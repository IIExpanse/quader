package ru.expanse;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.expanse.broker.BrokerAdapter;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;

import java.time.Duration;

@QuarkusMain
@SuppressWarnings("unused")
@RequiredArgsConstructor
@Slf4j
public class TradingBotApp implements QuarkusApplication {
    private final BrokerAdapter brokerAdapter;
    private static final String TICKER = "SBER";

    @Override
    public int run(String... args) {
        String instrumentId = brokerAdapter.findInstrumentId(TICKER)
                .await().atMost(Duration.ofSeconds(10));
        brokerAdapter.openBarsStreamForInstrument(instrumentId, SubscriptionInterval.SUBSCRIPTION_INTERVAL_ONE_MINUTE, 1)
                .subscribe().with(bar -> log.info("Received bar: {}", bar.toString()));
        Quarkus.waitForExit();
        return 0;
    }
}
