package eu.burakkocak.foreignexchangeapplication.mapper;

import eu.burakkocak.foreignexchangeapplication.api.dto.response.ExchangeRateResponse;
import eu.burakkocak.foreignexchangeapplication.api.dto.response.ExchangeRateTargetResponse;
import eu.burakkocak.foreignexchangeapplication.data.model.ExchangeRate;
import eu.burakkocak.foreignexchangeapplication.service.dto.ExchangeRateDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExchangeMapper {
    List<ExchangeRateTargetResponse> mapToExchangeRateTargetResponse(List<ExchangeRateDto> exchangeRateDto);
    ExchangeRateResponse mapToExchangeRateTargetResponse(ExchangeRateDto exchangeRateDto);
    ExchangeRate toExchangeRate(ExchangeRateDto exchangeRateDto);
    ExchangeRateDto toExchangeRateDto(ExchangeRate exchangeRate);
}
