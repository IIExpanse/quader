package ru.expanse.quader.bot.client;


import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.ta4j.core.Bar;
import ru.expanse.quader.bot.broker.BrokerAdapter;
import ru.expanse.quader.bot.factory.InstrumentRequestFactory;
import ru.expanse.quader.bot.mapper.TinkoffBarMapper;
import ru.tinkoff.piapi.contract.v1.*;

import java.util.Map;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class TinkoffClientAdapter implements BrokerAdapter {
    @GrpcClient("broker")
    MutinyMarketDataStreamServiceGrpc.MutinyMarketDataStreamServiceStub marketDataClient;
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

    public Multi<Bar> openBarsStreamForInstrument(Multi<MarketDataRequest> requestMulti, int lot) {
        return GrpcClientHelper.callWithHeaders(
                        marketDataClient,
                        requestMulti,
                        MutinyMarketDataStreamServiceGrpc.MutinyMarketDataStreamServiceStub::marketDataStream,
                        getAuthHeader()
                )
                .onFailure().retry().atMost(5)
                .invoke(response -> log.info("Received response: {}", response.toString()))
                .filter(MarketDataResponse::hasCandle)
                .map(MarketDataResponse::getCandle)
                .map(candle -> barMapper.toBar(candle, lot));
    }

    private Map<String, String> getAuthHeader() {
        return Map.of(AUTHORIZATION, BEARER_ + apiToken);
    }
}
