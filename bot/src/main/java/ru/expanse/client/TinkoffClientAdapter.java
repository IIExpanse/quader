package ru.expanse.client;


import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ta4j.core.Bar;
import ru.expanse.broker.BrokerAdapter;
import ru.expanse.factory.CandleRequestFactory;
import ru.expanse.mapper.TinkoffBarMapper;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.contract.v1.MarketDataStreamServiceClient;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;

@ApplicationScoped
public class TinkoffClientAdapter implements BrokerAdapter {
    @GrpcClient
    MarketDataStreamServiceClient marketDataClient;
    @Inject
    TinkoffBarMapper barMapper;

    public Multi<Bar> openBarsStreamForInstrument(String instrumentId, SubscriptionInterval interval, int lot) {

        return marketDataClient.marketDataServerSideStream(CandleRequestFactory
                .creaateDefaultCandleRequest(instrumentId, interval)
                ).map(MarketDataResponse::getCandle)
                .map(barMapper::toBar)
        );
    }
}
