package eu.burakkocak.foreignexchangeapplication.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "exchange_rates")
public class ExchangeRate {

    @Id
    private String id;
    private String currency;
    private BigDecimal rate;
    private LocalDate date;

}

