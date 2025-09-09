package ru.expanse.quader.bot.controller;

import com.google.protobuf.Empty;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import ru.expanse.quader.bot.service.ShutdownOperation;
import ru.expanse.quader.lib.api.proto.bot.ShutDownRequest;
import ru.expanse.quader.lib.api.proto.bot.ShutDownService;

@GrpcService
@RequiredArgsConstructor
public class ShutDownController implements ShutDownService {
    private final ShutdownOperation shutdownOperation;

    @Override
    public Uni<Empty> shutDown(ShutDownRequest request) {
        return shutdownOperation.shutDown(request.getOperationKey())
                .replaceWith(Empty.newBuilder().build());
    }
}
