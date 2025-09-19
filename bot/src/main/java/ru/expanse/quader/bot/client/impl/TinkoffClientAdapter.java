package ru.expanse.quader.bot.client.impl;


import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.ta4j.core.Bar;
import ru.expanse.quader.bot.client.BrokerAdapter;
import ru.expanse.quader.bot.client.GrpcClientHelper;
import ru.expanse.quader.bot.factory.InstrumentRequestFactory;
import ru.expanse.quader.bot.mapper.TinkoffBarMapper;
import ru.tinkoff.piapi.contract.v1.*;

import java.util.Map;
import java.util.NoSuchElementException;

@ApplicationScoped
@RequiredArgsConstructor
@Log4j2
public class TinkoffClientAdapter implements BrokerAdapter {
    @GrpcClient("broker-client")
    MarketDataStreamService marketDataClient;
    @GrpcClient("broker-client")
    InstrumentsService instrumentsClient;
    @ConfigProperty(name = "broker-client.api.token")
    String apiToken;

    private final TinkoffBarMapper barMapper;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    public Uni<InstrumentShort> findFirstInstrumentByQuery(String query) {
        return Uni.createFrom().item(InstrumentRequestFactory
                        .createFindInstrumentRequest(query, InstrumentType.INSTRUMENT_TYPE_SHARE)
                )
                .invoke(() -> log.trace("Starting to send findFirstInstrumentByQuery request."))
                .flatMap(request -> GrpcClientHelper.callWithHeaders(
                                instrumentsClient,
                                request,
                                InstrumentsService::findInstrument,
                                getAuthHeader()
                        )
                )
                .onFailure().retry().atMost(5)
                .invoke(() -> log.trace("Received response from findFirstInstrumentByQuery request."))
                .invoke(response -> {
                    if (response.getInstrumentsCount() == 0) {
                        throw new NoSuchElementException(String.format("No instrument found for ticker %s", query));
                    }
                })
                .map(response -> response.getInstruments(0));
    }

    public Multi<Bar> openBarsStreamForInstrument(Multi<MarketDataRequest> requestMulti, int lot) {
        return GrpcClientHelper.callWithHeaders(
                        marketDataClient,
                        requestMulti,
                        MarketDataStreamService::marketDataStream,
                        getAuthHeader()
                )
                .onFailure().retry().atMost(5)
                .invoke(response -> log.debug("Received response: {}", response.toString()))
                .filter(MarketDataResponse::hasCandle)
                .map(MarketDataResponse::getCandle)
                .map(candle -> barMapper.toBar(candle, lot));
    }

    private Map<String, String> getAuthHeader() {
        return Map.of(AUTHORIZATION, BEARER + apiToken);
    }
}
