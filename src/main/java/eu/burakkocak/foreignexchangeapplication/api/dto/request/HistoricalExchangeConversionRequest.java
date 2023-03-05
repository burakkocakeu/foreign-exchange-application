package eu.burakkocak.foreignexchangeapplication.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HistoricalExchangeConversionRequest extends HistoricalExchangeRateRequest {
    @NotNull
    private BigDecimal amount;
}
