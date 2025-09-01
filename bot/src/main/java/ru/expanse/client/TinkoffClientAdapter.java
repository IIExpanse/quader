package ru.expanse.client;


import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
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

import java.util.Map;

@ApplicationScoped
@RequiredArgsConstructor
public class TinkoffClientAdapter implements BrokerAdapter {
    @GrpcClient("broker")
    private final MarketDataStreamServiceClient marketDataClient;
    @GrpcClient("broker")
    private final InstrumentsServiceClient instrumentsClient;
    private final TinkoffBarMapper barMapper;

    @ConfigProperty(name = "broker.api.token")
    private String apiKey;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_ = "Bearer ";

    @Override
    public Uni<String> findInstrumentId(String ticker) {
        return Uni.createFrom().item(InstrumentRequestFactory
                        .createFindInstrumentRequest(ticker, InstrumentType.INSTRUMENT_TYPE_SHARE)
                )
                .flatMap(request -> GrpcClientHelper.callWithHeaders(
                                instrumentsClient,
                                request,
                                InstrumentsServiceClient::findInstrument,
                                getAuthHeader()
                        )
                )
                .onFailure().retry().atMost(5)
                .invoke(response -> {
                    if (response.getInstrumentsCount() == 0) {
                        throw new RuntimeException(String.format("No instrument found for ticker %s", ticker));
                    }
                })
                .map(response -> response.getInstruments(0).getUid());
    }

    public Multi<Bar> openBarsStreamForInstrument(String instrumentId, SubscriptionInterval interval, int lot) {
        return Uni.createFrom().item(CandleRequestFactory
                        .createDefaultCandleRequest(instrumentId, interval))
                .onItem().transformToMulti(request -> GrpcClientHelper.callWithHeaders(
                        marketDataClient,
                        request,
                        MarketDataStreamServiceClient::marketDataServerSideStream,
                        getAuthHeader()
                ))
                .onFailure().retry().atMost(5)
                .map(MarketDataResponse::getCandle)
                .map(barMapper::toBar);
    }

    private Map<String, String> getAuthHeader() {
        return Map.of(AUTHORIZATION, BEARER_ + apiKey);
    }
}
