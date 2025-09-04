package ru.expanse.quader.bot.mapper;

import org.mapstruct.*;
import org.ta4j.core.BaseBar;
import ru.tinkoff.piapi.contract.v1.Candle;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI,
        uses = BarTypesMapper.class)
public interface TinkoffBarMapper {
    @Mapping(target = "timePeriod", source = "candle.interval")
    @Mapping(target = "endTime", source = "candle")
    @Mapping(target = "openPrice", source = "candle.open")
    @Mapping(target = "highPrice", source = "candle.high")
    @Mapping(target = "lowPrice", source = "candle.low")
    @Mapping(target = "closePrice", source = "candle.close")
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "trades", ignore = true)
    BaseBar toBar(Candle candle, @Context int lot);
}
