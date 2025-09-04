package ru.expanse.quader.bot.broker;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.ta4j.core.Bar;
import ru.tinkoff.piapi.contract.v1.InstrumentShort;
import ru.tinkoff.piapi.contract.v1.MarketDataRequest;

public interface BrokerAdapter {
    Uni<InstrumentShort> findFirstInstrumentByQuery(String query);

    Multi<Bar> openBarsStreamForInstrument(Multi<MarketDataRequest> requestMulti, int lot);
}
