package ru.expanse.quader.controlpanel.client.impl;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import ru.expanse.quader.controlpanel.client.BotManagerClientAdapter;
import ru.expanse.quader.lib.api.proto.botmanager.ShutDownCommandService;
import ru.expanse.quader.lib.api.proto.botmanager.ShutDownRequest;

@ApplicationScoped
public class BotManagerClientAdapterImpl implements BotManagerClientAdapter {
    @GrpcClient("bot-manager-client")
    ShutDownCommandService shutdownClient;

    @Override
    public Uni<Void> shutDownBot(String botId) {
        return shutdownClient.shutDown(
                ShutDownRequest.newBuilder()
                        .setBotId(botId)
                        .build()
        ).replaceWithVoid();
    }
}
