package eu.burakkocak.foreignexchangeapplication.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ExchangeRateRequest {
    @NotNull
    private String source;
    private List<String> target;
}
