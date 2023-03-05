package eu.burakkocak.foreignexchangeapplication.api.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeConversionRequest extends ExchangeRateRequest {
    @NotNull
    private BigDecimal amount;

    @AssertTrue
    private boolean targetNotEmpty() {
        return super.getTarget() != null && super.getTarget().size() > 0;
    }
}
