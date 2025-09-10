package ru.expanse.quader.bot.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.ta4j.core.num.DecimalNumFactory;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;

import java.time.Duration;
import java.time.Instant;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface BarTypesMapper {
    NumFactory NUM_FACTORY = DecimalNumFactory.getInstance();

    default Duration toDuration(SubscriptionInterval interval) {
        return switch (interval) {
            case SUBSCRIPTION_INTERVAL_ONE_MINUTE -> Duration.ofMinutes(1);
            case SUBSCRIPTION_INTERVAL_2_MIN -> Duration.ofMinutes(2);
            case SUBSCRIPTION_INTERVAL_3_MIN -> Duration.ofMinutes(3);
            case SUBSCRIPTION_INTERVAL_FIVE_MINUTES -> Duration.ofMinutes(5);
            case SUBSCRIPTION_INTERVAL_10_MIN -> Duration.ofMinutes(10);
            case SUBSCRIPTION_INTERVAL_FIFTEEN_MINUTES -> Duration.ofMinutes(15);
            case SUBSCRIPTION_INTERVAL_30_MIN -> Duration.ofMinutes(30);
            case SUBSCRIPTION_INTERVAL_ONE_HOUR -> Duration.ofHours(1);
            case SUBSCRIPTION_INTERVAL_2_HOUR -> Duration.ofHours(2);
            case SUBSCRIPTION_INTERVAL_4_HOUR -> Duration.ofHours(4);
            case SUBSCRIPTION_INTERVAL_ONE_DAY -> Duration.ofDays(1);
            case SUBSCRIPTION_INTERVAL_WEEK -> Duration.ofDays(7);
            case SUBSCRIPTION_INTERVAL_MONTH -> Duration.ofDays(30);
            default -> throw new IllegalArgumentException("Unsupported interval: " + interval);
        };
    }

    default Instant toInstant(com.google.protobuf.Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }

    default Instant toEndTime(Candle candle) {
        if (candle == null) {
            return null;
        }
        return toInstant(candle.getTime())
                .plus(toDuration(candle.getInterval()));
    }

    default Num toNum(Quotation quotation, @Context int multiplier) {
        if (quotation == null) {
            return null;
        }
        return NUM_FACTORY.numOf(quotation.getUnits() + "." + quotation.getNano())
                .multipliedBy(NUM_FACTORY.numOf(multiplier));
    }

    default Num toNum(Number val) {
        if (val == null) {
            return null;
        }
        return NUM_FACTORY.numOf(val);
    }
}
