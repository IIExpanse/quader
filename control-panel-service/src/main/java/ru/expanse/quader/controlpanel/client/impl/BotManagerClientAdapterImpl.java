package ru.expanse.quader.controlpanel.client.impl;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.log4j.Log4j2;
import ru.expanse.quader.controlpanel.client.BotManagerClientAdapter;
import ru.expanse.quader.lib.api.proto.botmanager.ShutDownCommandService;
import ru.expanse.quader.lib.api.proto.botmanager.ShutDownRequest;

@ApplicationScoped
@Log4j2
public class BotManagerClientAdapterImpl implements BotManagerClientAdapter {
    @GrpcClient("bot-manager-client")
    ShutDownCommandService shutdownClient;

    @Override
    public Uni<Void> shutDownBot(String botId) {
        return Uni.createFrom().nullItem()
                .invoke(() -> log.trace("Started calling shutdown on manager for bot with botId={}", botId))
                .flatMap(empty -> shutdownClient.shutDown(
                        ShutDownRequest.newBuilder()
                                .setBotId(botId)
                                .build()
                ))
                .invoke(() -> log.trace("Finished calling shutdown on manager for bot with botId={}", botId))
                .replaceWithVoid();
    }
}
