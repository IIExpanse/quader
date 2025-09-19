package ru.expanse.quader.bot;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.Cancellable;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.expanse.quader.bot.client.BrokerAdapter;
import ru.expanse.quader.bot.factory.CandleRequestFactory;
import ru.tinkoff.piapi.contract.v1.InstrumentShort;
import ru.tinkoff.piapi.contract.v1.MarketDataRequest;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;

import java.time.Duration;
import java.util.concurrent.SubmissionPublisher;

@QuarkusMain
@SuppressWarnings("unused")
@RequiredArgsConstructor
@Log4j2
public class TradingBotApp implements QuarkusApplication {
    private final BrokerAdapter brokerAdapter;
    private static final String TICKER = "SBER";

    @Override
    public int run(String... args) {
        InstrumentShort instrument = brokerAdapter.findFirstInstrumentByQuery(TICKER)
                .await().atMost(Duration.ofSeconds(5));
        log.info("Found instrumentId for subscription: {}", instrument.getUid());
        Cancellable cancellable;

        try (SubmissionPublisher<MarketDataRequest> requestPublisher = new SubmissionPublisher<>()) {
            Multi<MarketDataRequest> requestMulti = Multi.createFrom().publisher(requestPublisher);

            log.info("Opening bars stream..");
            cancellable = brokerAdapter.openBarsStreamForInstrument(requestMulti, instrument.getLot())
                    .subscribe().with(bar -> log.info("Received bar: {}", bar.toString()),
                            error -> log.info("Received error: {}", error.toString()),
                            () -> log.info("Stream completed"));
            log.info("Bars stream opened. Submitting subscription request..");

            requestPublisher.submit(CandleRequestFactory.createDefaultCandleRequest(instrument.getUid(), SubscriptionInterval.SUBSCRIPTION_INTERVAL_ONE_MINUTE));
            log.info("Subscription request submitted. Main thread startup operations finished.");
            Quarkus.waitForExit();
            log.info("Exiting application..");
        }
        cancellable.cancel();
        return 0;
    }
}
