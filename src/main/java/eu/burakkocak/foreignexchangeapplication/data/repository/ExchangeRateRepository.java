package eu.burakkocak.foreignexchangeapplication.data.repository;

import eu.burakkocak.foreignexchangeapplication.data.model.ExchangeRate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends ReactiveMongoRepository<ExchangeRate, Long> {
    Flux<ExchangeRate> findByCurrencyInAndDate(List<String> currencies, LocalDate date);

    Flux<ExchangeRate> findAllByDate(LocalDate date);

    Flux<ExchangeRate> findAllByCurrencyInAndDateBetween(List<String> currencies, LocalDate start, LocalDate end, Pageable pageable);

    @Query(value = "{'currency' : { '$exists' : true}}", fields = "{ 'currency' : 1, 'id' : 0}")
    Flux<ExchangeRate> findDistinctCurrencyBy();

    @Query("{ 'currency': ?0, 'date': ?1 }")
    Mono<ExchangeRate> findByCurrencyAndDate(String currency, LocalDate date);

    /**
     * saveOrUpdate
     * @param exchangeRate must not be {@literal null}.
     * @return
     */
    default Mono<ExchangeRate> saveOrUpdate(ExchangeRate exchangeRate) {
        return findByCurrencyAndDate(exchangeRate.getCurrency(), exchangeRate.getDate())
                .flatMap(existingRate -> {
                    exchangeRate.setId(existingRate.getId());
                    return save(exchangeRate);
                })
                .switchIfEmpty(save(exchangeRate));
    }

    /**
     * saveOrUpdateAll
     * @param exchangeRates
     * @return
     */
    default Flux<ExchangeRate> saveAll(List<ExchangeRate> exchangeRates) {
        return saveAll(Flux.fromIterable(exchangeRates));
    }

    /**
     * saveOrUpdateAll
     * @param exchangeRates
     * @return
     */
    default Flux<ExchangeRate> saveAll(Flux<ExchangeRate> exchangeRates) {
        return exchangeRates.flatMap(exchangeRate ->
                findByCurrencyAndDate(exchangeRate.getCurrency(), exchangeRate.getDate())
                        .flatMap(existingRate -> {
                            exchangeRate.setId(existingRate.getId());
                            return save(exchangeRate);
                        })
                        .switchIfEmpty(save(exchangeRate))
        );
    }
}
