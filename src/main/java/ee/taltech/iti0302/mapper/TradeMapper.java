package ee.taltech.iti0302.mapper;

import ee.taltech.iti0302.dto.TradeDto;
import ee.taltech.iti0302.model.Trade;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TradeMapper {

    TradeDto entityToDto(Trade trade);
    Trade dtoToEntity(TradeDto tradeDto);

    List<TradeDto> toDtoList(List<Trade> trades);
}
