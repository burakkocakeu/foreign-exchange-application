package eu.burakkocak.foreignexchangeapplication.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto implements Serializable {
    private String currency;
    private BigDecimal rate;
    private LocalDate date;
}