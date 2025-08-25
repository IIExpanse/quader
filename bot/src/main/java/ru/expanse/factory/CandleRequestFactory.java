package ru.expanse.factory;

import ru.tinkoff.piapi.contract.v1.*;

public class CandleRequestFactory {
    public static MarketDataServerSideStreamRequest creaateDefaultCandleRequest(
            String instrumentId,
            SubscriptionInterval interval
    ) {
        SubscribeCandlesRequest request = SubscribeCandlesRequest.newBuilder()
                .setSubscriptionAction(SubscriptionAction.SUBSCRIPTION_ACTION_SUBSCRIBE)
                .addInstruments(createInstrument(instrumentId, interval))
                .setWaitingClose(true)
                .build();

        return MarketDataServerSideStreamRequest.newBuilder()
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
