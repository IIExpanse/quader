package ru.expanse.broker;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.ta4j.core.Bar;
import ru.tinkoff.piapi.contract.v1.InstrumentShort;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;

public interface BrokerAdapter {
    Uni<InstrumentShort> findFirstInstrumentByQuery(String query);

    Multi<Bar> openBarsStreamForInstrument(String instrumentId, SubscriptionInterval interval, int lot);
}
