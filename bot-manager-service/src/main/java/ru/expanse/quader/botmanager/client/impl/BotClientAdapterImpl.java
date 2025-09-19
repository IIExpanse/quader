package ru.expanse.quader.botmanager.client.impl;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.expanse.quader.botmanager.client.BotClientAdapter;
import ru.expanse.quader.lib.api.proto.bot.ShutDownBotService;
import ru.expanse.quader.lib.api.proto.bot.ShutDownRequest;

@ApplicationScoped
@RequiredArgsConstructor
@Log4j2
public class BotClientAdapterImpl implements BotClientAdapter {
    @GrpcClient("bot-client")
    ShutDownBotService shutDownClient;

    @Override
    public Uni<Void> shutDownBot(String operationKey) {
        return Uni.createFrom().nullItem()
                .invoke(() -> log.trace("Starting to call shutdown request on bot with operationKey={}", operationKey))
                .flatMap(empty -> shutDownClient.shutDown(
                                ShutDownRequest.newBuilder()
                                        .setOperationKey(operationKey)
                                        .build()
                        )
                )
                .invoke(() -> log.trace("Finished calling shutdown request on bot with operationKey={}", operationKey))
                .replaceWithVoid();
    }
}
