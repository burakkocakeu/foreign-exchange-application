package eu.burakkocak.foreignexchangeapplication.api.controller;

import eu.burakkocak.foreignexchangeapplication.api.dto.request.ExchangeConversionRequest;
import eu.burakkocak.foreignexchangeapplication.api.dto.request.HistoricalExchangeConversionRequest;
import eu.burakkocak.foreignexchangeapplication.api.dto.request.HistoricalExchangeRateRequest;
import eu.burakkocak.foreignexchangeapplication.api.dto.response.ExchangeConversionResponse;
import eu.burakkocak.foreignexchangeapplication.api.dto.response.ExchangeRateResponse;
import eu.burakkocak.foreignexchangeapplication.data.model.ExchangeRate;
import eu.burakkocak.foreignexchangeapplication.service.ExchangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExchangeController {
    private final ExchangeService exchangeService;

    /**
     * This endpoint adds manual exchange rates into database
     *  IMPORTANT: list should be provided by source = 'USD'.!!!
     * @param rates
     * @return
     */
    @PostMapping("/exchange/rates")
    public Flux<ExchangeRate> addExchangeRatesList(@RequestBody List<ExchangeRate> rates) {
        return exchangeService.addExchangeRatesList(rates);
    }

    /**
     * This endpoint returns ALL or SOME of the currencies from cache.
     * This data is retrieved once a day and might be stored up to 6h, not live data.
     *  to change cache's expire time -> (see: spring.cache.caffeine.spec on 'application.yml')
     * @param currencies (OPTIONAL)
     * @return
     */
    @GetMapping(value = "/exchange/rates", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<ExchangeRateResponse> getExchangeRatesList(@RequestParam String base,
                                                           @RequestParam(required = false) List<String> currencies) {
        return exchangeService.getExchangeRatesList(base, currencies);
    }

    /**
     * This endpoint returns historical currency exchange rates for a given date range.
     * @param request
     * @param pageable
     * @return
     */
    @GetMapping("/exchange/rates/historical")
    public Mono<ExchangeRateResponse> getExchangeRatesHistorical(@Valid HistoricalExchangeRateRequest request, Pageable pageable) {
        return exchangeService.getExchangeRatesHistorical(request, pageable);
    }

    /**
     * This endpoint allows converting an amount from one currency
     *  to another based on the latest exchange rates.
     * Getting (1..N) target to convert amount by source currency, and it's amount
     * @param request
     * @return
     */
    @GetMapping("/exchange/convert")
    public Mono<ExchangeConversionResponse> getExchangeConvert(@Valid ExchangeConversionRequest request) {
        return exchangeService.getExchangeConvert(request);
    }

    /**
     * This endpoint allows converting an amount from one currency
     *  to another based on historical exchange rates for a given date range.
     * @param request
     * @param pageable
     * @return
     */
    @GetMapping("/exchange/convert/historical")
    public Mono<ExchangeConversionResponse> getExchangeConvertHistorical(@Valid HistoricalExchangeConversionRequest request,
                                                                          Pageable pageable) {
        return exchangeService.getExchangeConvertHistorical(request, pageable);
    }
}
