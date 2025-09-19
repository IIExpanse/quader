package ru.expanse.quader.botmanager.controller;

import com.google.protobuf.Empty;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.expanse.quader.botmanager.service.BotService;
import ru.expanse.quader.lib.api.proto.botmanager.ShutDownCommandService;
import ru.expanse.quader.lib.api.proto.botmanager.ShutDownRequest;

@GrpcService
@RequiredArgsConstructor
@Log4j2
public class BotController implements ShutDownCommandService {
    private final BotService botService;

    @Override
    public Uni<Empty> shutDown(ShutDownRequest request) {
        return Uni.createFrom().nullItem()
                .invoke(() -> log.info("Received shutdown request for bot with id={}", request.getBotId()))
                .flatMap(empty -> botService.shutDownBot(request.getBotId()))
                .invoke(() -> log.info("Finished processing shutdown for bot with id={}", request.getBotId()))
                .replaceWith(Empty.newBuilder().build());
    }
}
