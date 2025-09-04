package ru.expanse.quader.bot.controller;

import com.google.protobuf.Empty;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import ru.expanse.quader.bot.api.contract.ShutDownRequest;
import ru.expanse.quader.bot.api.contract.ShutDownService;
import ru.expanse.quader.bot.service.ShutdownService;

@GrpcService
@RequiredArgsConstructor
public class ShutDownController implements ShutDownService {
    private final ShutdownService shutdownService;

    @Override
    public Uni<Empty> shutDown(ShutDownRequest request) {
        return shutdownService.shutDown(request.getOperationKey())
                .replaceWith(Empty.newBuilder().build());
    }
}
