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
import ru.tinkoff.piapi.contract.v1.*;

import java.util.Map;

@ApplicationScoped
@RequiredArgsConstructor
public class TinkoffClientAdapter implements BrokerAdapter {
    @GrpcClient("broker")
    MarketDataStreamService marketDataClient;
    @GrpcClient("broker")
    InstrumentsService instrumentsClient;
    @ConfigProperty(name = "broker.api.token")
    String apiToken;

    private final TinkoffBarMapper barMapper;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_ = "Bearer ";

    @Override
    public Uni<InstrumentShort> findFirstInstrumentByQuery(String query) {
        return Uni.createFrom().item(InstrumentRequestFactory
                        .createFindInstrumentRequest(query, InstrumentType.INSTRUMENT_TYPE_SHARE)
                )
                .flatMap(request -> GrpcClientHelper.callWithHeaders(
                                instrumentsClient,
                                request,
                                InstrumentsService::findInstrument,
                                getAuthHeader()
                        )
                )
                .onFailure().retry().atMost(5)
                .invoke(response -> {
                    if (response.getInstrumentsCount() == 0) {
                        throw new RuntimeException(String.format("No instrument found for ticker %s", query));
                    }
                })
                .map(response -> response.getInstruments(0));
    }

    public Multi<Bar> openBarsStreamForInstrument(String instrumentId, SubscriptionInterval interval, int lot) {
        return Uni.createFrom().item(CandleRequestFactory
                        .createDefaultCandleRequest(instrumentId, interval))
                .onItem().transformToMulti(request -> GrpcClientHelper.callWithHeaders(
                        marketDataClient,
                        request,
                        MarketDataStreamService::marketDataServerSideStream,
                        getAuthHeader()
                ))
                .onFailure().retry().atMost(5)
                .filter(MarketDataResponse::hasCandle)
                .map(MarketDataResponse::getCandle)
                .map(candle -> barMapper.toBar(candle, lot));
    }

    private Map<String, String> getAuthHeader() {
        return Map.of(AUTHORIZATION, BEARER_ + apiToken);
    }
}
