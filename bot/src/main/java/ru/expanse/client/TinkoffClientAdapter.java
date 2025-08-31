package ru.expanse.client;


import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.ta4j.core.Bar;
import ru.expanse.broker.BrokerAdapter;
import ru.expanse.factory.CandleRequestFactory;
import ru.expanse.factory.InstrumentRequestFactory;
import ru.expanse.mapper.TinkoffBarMapper;
import ru.tinkoff.piapi.contract.v1.InstrumentType;
import ru.tinkoff.piapi.contract.v1.InstrumentsServiceClient;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.contract.v1.MarketDataStreamServiceClient;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;

@ApplicationScoped
@RequiredArgsConstructor
public class TinkoffClientAdapter implements BrokerAdapter {
    @GrpcClient
    private final MarketDataStreamServiceClient marketDataClient;
    @GrpcClient
    private final InstrumentsServiceClient instrumentsClient;
    private final TinkoffBarMapper barMapper;

    @Override
    public Uni<String> findInstrumentId(String ticker) {
        return instrumentsClient.findInstrument(
                        InstrumentRequestFactory.createFindInstrumentRequest(ticker, InstrumentType.INSTRUMENT_TYPE_SHARE)
                ).invoke(response -> {
                    if (response.getInstrumentsCount() == 0) {
                        throw new RuntimeException(String.format("No instrument found for ticker %s", ticker));
                    }
                })
                .map(response -> response.getInstruments(0).getUid());
    }

    public Multi<Bar> openBarsStreamForInstrument(String instrumentId, SubscriptionInterval interval, int lot) {

        return marketDataClient.marketDataServerSideStream(CandleRequestFactory
                        .createDefaultCandleRequest(instrumentId, interval)
                ).map(MarketDataResponse::getCandle)
                .map(barMapper::toBar);
    }
}
