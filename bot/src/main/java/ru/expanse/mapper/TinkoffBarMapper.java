package ru.expanse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.ta4j.core.BaseBar;
import ru.tinkoff.piapi.contract.v1.Candle;

import java.time.Instant;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI,
        uses = BarTypesMapper.class)
public interface TinkoffBarMapper {
    @Mapping(target = "timePeriod", source = "candle.interval")
    @Mapping(target = "endTime", source = "candle")
    @Mapping(target = "openPrice", source = "candle.open")
    @Mapping(target = "highPrice", source = "candle.high")
    @Mapping(target = "lowPrice", source = "candle.low")
    @Mapping(target = "closePrice", source = "candle.close")
    BaseBar toBar(Candle candle);

    default Instant toEndTime(Candle candle) {
        BarTypesMapper barTypesMapper = Mappers.getMapper(BarTypesMapper.class);
        return barTypesMapper.toInstant(candle.getTime())
                .plus(barTypesMapper.toDuration(candle.getInterval()));
    }
}
