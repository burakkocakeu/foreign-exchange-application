package eu.burakkocak.foreignexchangeapplication.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeConversionTargetResponse {
    private String currency;
    private BigDecimal amount;
    private BigDecimal rate;
    private LocalDate date;
}
