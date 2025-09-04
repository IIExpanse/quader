package ru.expanse.quader.bot.factory;

import ru.tinkoff.piapi.contract.v1.*;

public class CandleRequestFactory {
    private CandleRequestFactory() {}

    public static MarketDataRequest createDefaultCandleRequest(
            String instrumentId,
            SubscriptionInterval interval
    ) {
        SubscribeCandlesRequest request = SubscribeCandlesRequest.newBuilder()
                .setSubscriptionAction(SubscriptionAction.SUBSCRIPTION_ACTION_SUBSCRIBE)
                .addInstruments(createInstrument(instrumentId, interval))
                .setWaitingClose(true)
                .build();

        return MarketDataRequest.newBuilder()
                .setSubscribeCandlesRequest(request)
                .build();
    }

    private static CandleInstrument createInstrument(String instrumentId, SubscriptionInterval interval) {
        return CandleInstrument.newBuilder()
                .setInstrumentId(instrumentId)
                .setInterval(interval)
                .build();
    }
}
