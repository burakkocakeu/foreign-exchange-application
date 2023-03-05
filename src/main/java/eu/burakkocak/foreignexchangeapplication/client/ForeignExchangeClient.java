package eu.burakkocak.foreignexchangeapplication.client;

import eu.burakkocak.foreignexchangeapplication.client.response.ExchangeRatesDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "exchange-service", url = "${foreign-exchange.service.host}")
public interface ForeignExchangeClient {
    @GetMapping("/exchangerates_data/latest")
    ExchangeRatesDataResponse getExchangeRatesData(@RequestHeader(name = "apikey") String apiKey,
                                                   @RequestParam List<String> symbols,
                                                   @RequestParam String base);
}
