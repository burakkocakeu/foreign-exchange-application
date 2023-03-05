package eu.burakkocak.foreignexchangeapplication.load;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.burakkocak.foreignexchangeapplication.data.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateDataInitializer implements CommandLineRunner {
    private final ObjectMapper mapper;
    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public void run(String... args) throws Exception {
        /*exchangeRateRepository.deleteAll();
        try (InputStream inputStream = new ClassPathResource("db/exchange-rates.json").getInputStream()) {
            List<ExchangeRate> exchangeRates = mapper.readValue(inputStream, new TypeReference<>() {});
            Flux<ExchangeRate> exchangeRateFlux = exchangeRateRepository.saveAll(exchangeRates);
            exchangeRateFlux
                    .collectList()
                    .doOnSuccess(list -> log.info("Saved {} exchange rates to the database.", list.size()))
                    .subscribe();
        } catch (IOException e) {
            log.error("Failed to load exchange rates from file: {}", e.getMessage());
        }
        log.info("Exchange rates saved!");*/
    }
}

