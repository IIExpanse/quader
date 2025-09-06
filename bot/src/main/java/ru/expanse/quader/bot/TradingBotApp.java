package ru.expanse.quader.bot;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.Cancellable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.expanse.quader.bot.broker.BrokerAdapter;
import ru.expanse.quader.bot.factory.CandleRequestFactory;
import ru.tinkoff.piapi.contract.v1.InstrumentShort;
import ru.tinkoff.piapi.contract.v1.MarketDataRequest;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;

import java.time.Duration;
import java.util.concurrent.SubmissionPublisher;

@QuarkusMain
@SuppressWarnings("unused")
@RequiredArgsConstructor
@Slf4j
public class TradingBotApp implements QuarkusApplication {
    private final BrokerAdapter brokerAdapter;
    private static final String TICKER = "SBER";

    @Override
    public int run(String... args) {
        InstrumentShort instrument = brokerAdapter.findFirstInstrumentByQuery(TICKER)
                .await().atMost(Duration.ofSeconds(5));
        Cancellable cancellable;

        try (SubmissionPublisher<MarketDataRequest> requestPublisher = new SubmissionPublisher<>()) {
            Multi<MarketDataRequest> requestMulti = Multi.createFrom().publisher(requestPublisher);

            cancellable = brokerAdapter.openBarsStreamForInstrument(requestMulti, instrument.getLot())
                    .subscribe().with(bar -> log.info("Received bar: {}", bar.toString()),
                            error -> log.info("Received error: {}", error.toString()),
                            () -> log.info("Stream completed"));

            requestPublisher.submit(CandleRequestFactory.createDefaultCandleRequest(instrument.getUid(), SubscriptionInterval.SUBSCRIPTION_INTERVAL_ONE_MINUTE));
            Quarkus.waitForExit();
        }
        cancellable.cancel();
        return 0;
    }
}
