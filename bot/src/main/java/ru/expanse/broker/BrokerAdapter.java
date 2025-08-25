package ru.expanse.broker;

import io.smallrye.mutiny.Multi;
import org.ta4j.core.Bar;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;

public interface BrokerAdapter {
    Multi<Bar> openBarsStreamForInstrument(String instrumentId, SubscriptionInterval interval, int lot);
}
