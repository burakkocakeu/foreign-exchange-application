package eu.burakkocak.foreignexchangeapplication.service;

import eu.burakkocak.foreignexchangeapplication.api.dto.request.ExchangeConversionRequest;
import eu.burakkocak.foreignexchangeapplication.api.dto.request.HistoricalExchangeConversionRequest;
import eu.burakkocak.foreignexchangeapplication.api.dto.request.HistoricalExchangeRateRequest;
import eu.burakkocak.foreignexchangeapplication.api.dto.response.ExchangeConversionResponse;
import eu.burakkocak.foreignexchangeapplication.api.dto.response.ExchangeRateResponse;
import eu.burakkocak.foreignexchangeapplication.data.model.ExchangeRate;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ExchangeService {
    Flux<ExchangeRate> addExchangeRatesList(List<ExchangeRate> rates);
    Mono<ExchangeRateResponse> getExchangeRatesList(String base, List<String> currencies);
    Mono<ExchangeRateResponse> getExchangeRatesHistorical(HistoricalExchangeRateRequest request, Pageable pageable);
    Mono<ExchangeConversionResponse> getExchangeConvert(ExchangeConversionRequest request);
    Mono<ExchangeConversionResponse> getExchangeConvertHistorical(HistoricalExchangeConversionRequest request, Pageable pageable);
}
