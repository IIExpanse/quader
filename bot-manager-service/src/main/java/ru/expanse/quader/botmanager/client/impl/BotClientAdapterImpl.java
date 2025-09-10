package ru.expanse.quader.botmanager.client.impl;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.expanse.quader.botmanager.client.BotClientAdapter;
import ru.expanse.quader.lib.api.proto.bot.ShutDownBotService;
import ru.expanse.quader.lib.api.proto.bot.ShutDownRequest;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class BotClientAdapterImpl implements BotClientAdapter {
    @GrpcClient("bot-client")
    ShutDownBotService shutDownClient;

    @Override
    public Uni<Void> shutDownBot(String operationKey) {
        return Uni.createFrom().nullItem()
                .flatMap(empty -> shutDownClient.shutDown(
                                ShutDownRequest.newBuilder()
                                        .setOperationKey(operationKey)
                                        .build()
                        ).replaceWithVoid()
                );
    }
}
