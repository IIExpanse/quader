package ru.expanse.quader.bot.factory;

import ru.tinkoff.piapi.contract.v1.FindInstrumentRequest;
import ru.tinkoff.piapi.contract.v1.InstrumentType;

public class InstrumentRequestFactory {
    private InstrumentRequestFactory() {}

    public static FindInstrumentRequest createFindInstrumentRequest(String ticker, InstrumentType instrumentType) {
        return FindInstrumentRequest.newBuilder()
                .setQuery(ticker)
                .setApiTradeAvailableFlag(true)
                .setInstrumentKind(instrumentType)
                .build();
    }
}
