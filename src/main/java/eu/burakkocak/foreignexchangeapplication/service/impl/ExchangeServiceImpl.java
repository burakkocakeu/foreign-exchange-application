package eu.burakkocak.foreignexchangeapplication.service.impl;

import eu.burakkocak.foreignexchangeapplication.api.dto.request.ExchangeConversionRequest;
import eu.burakkocak.foreignexchangeapplication.api.dto.request.HistoricalExchangeConversionRequest;
import eu.burakkocak.foreignexchangeapplication.api.dto.request.HistoricalExchangeRateRequest;
import eu.burakkocak.foreignexchangeapplication.api.dto.response.ExchangeConversionResponse;
import eu.burakkocak.foreignexchangeapplication.api.dto.response.ExchangeConversionTargetResponse;
import eu.burakkocak.foreignexchangeapplication.api.dto.response.ExchangeRateResponse;
import eu.burakkocak.foreignexchangeapplication.api.dto.response.ExchangeRateTargetResponse;
import eu.burakkocak.foreignexchangeapplication.client.ForeignExchangeClient;
import eu.burakkocak.foreignexchangeapplication.client.response.ExchangeRatesDataResponse;
import eu.burakkocak.foreignexchangeapplication.data.model.ExchangeRate;
import eu.burakkocak.foreignexchangeapplication.data.repository.ExchangeRateRepository;
import eu.burakkocak.foreignexchangeapplication.mapper.ExchangeMapper;
import eu.burakkocak.foreignexchangeapplication.service.ExchangeService;
import eu.burakkocak.foreignexchangeapplication.service.dto.ExchangeRateDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static eu.burakkocak.foreignexchangeapplication.util.Constants.USD;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    @Value("${foreign-exchange.service.api-key}")
    private String apiKey;

    private final ExchangeMapper mapper;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ForeignExchangeClient foreignExchangeClient;

    @Override
    public Flux<ExchangeRate> addExchangeRatesList(List<ExchangeRate> rates) {
        rates.forEach(exchange -> exchange.setCurrency(exchange.getCurrency().toLowerCase()));
        return exchangeRateRepository.saveAll(rates);
    }

    @Override
    @Retryable
    @CircuitBreaker(name = "clientExchangeRatesCircuitBreaker", fallbackMethod = "getClientExchangeRatesDataFallback")
    public Mono<ExchangeRateResponse> getExchangeRatesList(String base, List<String> currencies) {
        // Get rates Flux and baseRate
        List<ExchangeRateDto> allCurrencies = getLocalExchangeRatesList()
                .collectList().block()
                .stream().map(mapper::toExchangeRateDto).toList();

        Flux<ExchangeRateDto> rates = allCurrencies.isEmpty() ?
                getClientExchangeRatesData().map(mapper::toExchangeRateDto) :
                Flux.fromIterable(allCurrencies);

        ExchangeRateDto baseRate = rates.filter(rate -> rate.getCurrency().equalsIgnoreCase(base))
                .switchIfEmpty(Mono.error(new RuntimeException("Base currency not found or outdated!")))
                .blockFirst();

        // Reduce rates list if currencies param is not empty in incoming request
        if (Objects.nonNull(currencies) && !currencies.isEmpty()) {
            currencies.add(base);
            List<String> lowercaseCurrencies = currencies.stream().map(String::toLowerCase).toList();
            rates = rates.filter(rate -> lowercaseCurrencies.contains(rate.getCurrency()));
        }

        // Filter out base currency from 'rates' currency list
        rates = rates.filter(rate -> !baseRate.getCurrency().equalsIgnoreCase(rate.getCurrency()));

        // Recalculate all rates by 'base' currency
        if (baseRate != null && !baseRate.getCurrency().equalsIgnoreCase(USD)) {
            rates = rates.map(rate -> {
                BigDecimal rateValue = rate.getRate().divide(baseRate.getRate(), new MathContext(6, RoundingMode.HALF_UP));
                return ExchangeRateDto.builder()
                        .currency(rate.getCurrency())
                        .rate(rateValue)
                        .date(rate.getDate())
                        .build();
            });
        }

        return Mono.just(ExchangeRateResponse.builder()
                .currency(base)
                .targets(mapper.mapToExchangeRateTargetResponse(rates.collectList().block()))
                .build());
    }

    /**
     * Get all exchange rates from remote client if local database does not have today's rates already retrieved.
     *
     * @return
     */
    private Flux<ExchangeRate> getClientExchangeRatesData() {
        LocalDate now = LocalDate.now();
        ExchangeRatesDataResponse response = foreignExchangeClient
                .getExchangeRatesData(apiKey, List.of(), USD); /** Getting all 170 currencies sending empty "symbols" param. */
        log.info("ExchangeRatesDataResponse: ", response);
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        response.getRates()
                .forEach((currency, rate) -> exchangeRates.add(ExchangeRate.builder()
                        .currency(currency.toLowerCase())
                        .rate(rate)
                        .date(now).build()));
        return exchangeRateRepository.saveAll(exchangeRates);
    }

    /**
     * This fallback method will be returning a default value in case of an error on getExchangeRatesList(),
     * and consumed all retries defined by @Retryable
     *
     * @param currencies
     * @param ex
     * @return
     */
    public Mono<ExchangeRateResponse> getClientExchangeRatesDataFallback(String base, List<String> currencies, Exception ex) {
        log.info("ClientExchangeRatesCircuitBreaker triggered on getClientExchangeRatesDataFallback(), Ex: ", ex);
        return Mono.just(new ExchangeRateResponse());
    }

    /**
     * Find all currencies by today's date and cache them for some time.
     * (see: spring.cache.caffeine.spec on 'application.yml')
     *
     * @return
     */
    //@Cacheable(CURRENCIES_CACHE_KEY) //TODO: will activate caching later on!
    public Flux<ExchangeRate> getLocalExchangeRatesList() {
        return exchangeRateRepository.findAllByDate(LocalDate.now()); //.cache();
    }

    @Override
    public Mono<ExchangeRateResponse> getExchangeRatesHistorical(HistoricalExchangeRateRequest request, Pageable pageable) {
        request.setSource(request.getSource().toLowerCase());
        request.setTarget(request.getTarget().toLowerCase());
        List<String> currencies = List.of(request.getSource(), request.getTarget());

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("date"), Sort.Order.asc("currency")));
        Flux<ExchangeRate> rates = exchangeRateRepository.findAllByCurrencyInAndDateBetween(
                currencies,
                request.getStart(),
                request.getEnd(),
                pageRequest
        );

        Flux<ExchangeRate> baseRates = rates.filter(rate -> rate.getCurrency().equalsIgnoreCase(request.getSource()));

        return rates
                .filter(response -> !response.getCurrency().equals(request.getSource())) // Exclude the source currency
                .groupBy(ExchangeRate::getDate)
                .flatMap(groupedFlux -> {
                    Flux<ExchangeRate> baseRate = baseRates.filter(rate -> rate.getDate().equals(groupedFlux.key()))
                            .switchIfEmpty(Mono.empty()); // Return an empty Mono if base currency not found or outdated

                    return groupedFlux
                            .flatMap(exchangeRate -> baseRate
                                    .map(rate -> {
                                        BigDecimal rateValue = exchangeRate.getRate().divide(rate.getRate(), new MathContext(6, RoundingMode.HALF_UP));
                                        return ExchangeRateTargetResponse.builder()
                                                .currency(exchangeRate.getCurrency())
                                                .rate(rateValue)
                                                .date(exchangeRate.getDate())
                                                .build();
                                    })
                            );
                })
                .onErrorResume(e -> Mono.error(new RuntimeException("An error occurred while processing the request", e)))
                .collectList()
                .map(exchangeRateTargetResponses -> ExchangeRateResponse.builder()
                        .currency(request.getSource())
                        .targets(exchangeRateTargetResponses)
                        .build());
    }

    @Override
    public Mono<ExchangeConversionResponse> getExchangeConvert(ExchangeConversionRequest request) {
        request.setSource(request.getSource().toLowerCase());
        List<String> targetCurrencies = request.getTarget().stream().map(String::toLowerCase).toList();
        List<String> allCurrencies = Stream.concat(targetCurrencies.stream(), Stream.of(request.getSource())).toList();

        return exchangeRateRepository.findByCurrencyInAndDate(allCurrencies, LocalDate.now())
                .collectMap(ExchangeRate::getCurrency)
                .flatMap(map -> {
                    ExchangeRate sourceRate = map.get(request.getSource());

                    if (sourceRate == null) {
                        return Mono.error(new RuntimeException("Source currency not found or outdated!"));
                    }

                    ExchangeConversionResponse response = ExchangeConversionResponse.builder()
                            .currency(request.getSource())
                            .amount(request.getAmount())
                            .targets(new ArrayList<>())
                            .build();

                    for (String targetCurrency : targetCurrencies) {
                        ExchangeRate targetRate = map.get(targetCurrency);
                        if (targetRate == null) {
                            return Mono.error(new RuntimeException("Target currency not found or outdated!"));
                        }

                        BigDecimal amount = request.getAmount().multiply(targetRate.getRate())
                                .divide(sourceRate.getRate(), new MathContext(6, RoundingMode.HALF_UP));
                        BigDecimal rateValue = targetRate.getRate()
                                .divide(sourceRate.getRate(), new MathContext(6, RoundingMode.HALF_UP));

                        response.getTargets().add(ExchangeConversionTargetResponse.builder()
                                .currency(targetRate.getCurrency())
                                .amount(amount)
                                .rate(rateValue)
                                .date(targetRate.getDate())
                                .build());
                    }

                    return Mono.just(response);
                })
                .onErrorResume(e -> Mono.error(new RuntimeException("An error occurred while processing the request", e)));
    }

    @Override
    public Mono<ExchangeConversionResponse> getExchangeConvertHistorical(HistoricalExchangeConversionRequest request, Pageable pageable) {
        request.setSource(request.getSource().toLowerCase());
        request.setTarget(request.getTarget().toLowerCase());
        List<String> currencies = List.of(request.getSource(), request.getTarget());

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("date"), Sort.Order.asc("currency")));
        Flux<ExchangeRate> rates = exchangeRateRepository.findAllByCurrencyInAndDateBetween(
                currencies,
                request.getStart(),
                request.getEnd(),
                pageRequest
        );

        Flux<ExchangeRate> baseRates = rates.filter(rate -> rate.getCurrency().equalsIgnoreCase(request.getSource()));

        return rates
                .filter(response -> !response.getCurrency().equals(request.getSource())) // Exclude the source currency
                .groupBy(ExchangeRate::getDate)
                .flatMap(groupedFlux -> {
                    Mono<ExchangeRate> baseRate = baseRates.filter(rate -> rate.getDate().equals(groupedFlux.key()))
                            .switchIfEmpty(Mono.error(new RuntimeException("Base currency not found or outdated!")))
                            .next();

                    return groupedFlux
                            .flatMap(exchangeRate -> baseRate
                                    .map(rate -> {
                                        BigDecimal amount = request.getAmount().multiply(exchangeRate.getRate())
                                                .divide(baseRate.block().getRate(), new MathContext(6, RoundingMode.HALF_UP));
                                        BigDecimal rateValue = exchangeRate.getRate()
                                                .divide(rate.getRate(), new MathContext(6, RoundingMode.HALF_UP));

                                        return ExchangeConversionTargetResponse.builder()
                                                .currency(exchangeRate.getCurrency())
                                                .amount(amount)
                                                .rate(rateValue)
                                                .date(exchangeRate.getDate())
                                                .build();
                                    })
                            );
                })
                .onErrorResume(e -> Mono.error(new RuntimeException("An error occurred while processing the request", e)))
                .collectList()
                .map(exchangeConversationTargetResponses -> ExchangeConversionResponse.builder()
                        .currency(request.getSource())
                        .amount(request.getAmount())
                        .targets(exchangeConversationTargetResponses)
                        .build());
    }
}
