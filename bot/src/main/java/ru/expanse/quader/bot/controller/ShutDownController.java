package ru.expanse.quader.bot.controller;

import com.google.protobuf.Empty;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.expanse.quader.bot.service.ShutdownOperation;
import ru.expanse.quader.lib.api.proto.bot.ShutDownRequest;
import ru.expanse.quader.lib.api.proto.bot.ShutDownBotService;

@GrpcService
@RequiredArgsConstructor
@Log4j2
public class ShutDownController implements ShutDownBotService {
    private final ShutdownOperation shutdownOperation;

    @Override
    public Uni<Empty> shutDown(ShutDownRequest request) {
        return Uni.createFrom().nullItem()
                .invoke(() -> log.info("Received shutdown request with operationKey={}", request.getOperationKey()))
                .flatMap(empty -> shutdownOperation.shutDown(request.getOperationKey()))
                .invoke(() -> log.info("Finished processing shutdown request"))
                .replaceWith(Empty.newBuilder().build());
    }
}
