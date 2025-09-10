package ru.expanse.quader.botmanager.controller;

import com.google.protobuf.Empty;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import ru.expanse.quader.botmanager.service.BotService;
import ru.expanse.quader.lib.api.proto.botmanager.ShutDownCommandService;
import ru.expanse.quader.lib.api.proto.botmanager.ShutDownRequest;

@GrpcService
@RequiredArgsConstructor
public class BotController implements ShutDownCommandService {
    private final BotService botService;

    @Override
    public Uni<Empty> shutDown(ShutDownRequest request) {
        return botService.shutDownBot(request.getBotId())
                .replaceWith(Empty.newBuilder().build());
    }
}
