package eu.burakkocak.foreignexchangeapplication.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeConversionResponse {
    private String currency;
    private BigDecimal amount;
    @Builder.Default
    private List<ExchangeConversionTargetResponse> targets = new ArrayList<>();
}
