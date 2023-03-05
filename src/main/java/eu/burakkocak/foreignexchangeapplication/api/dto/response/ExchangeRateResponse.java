package eu.burakkocak.foreignexchangeapplication.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {
    private String currency;
    @Builder.Default
    private List<ExchangeRateTargetResponse> targets = new ArrayList<>();
}
