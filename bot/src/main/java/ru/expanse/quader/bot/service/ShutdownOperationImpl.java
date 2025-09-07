package ru.expanse.quader.bot.service;

import io.quarkus.runtime.Quarkus;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShutdownOperationImpl implements ShutdownOperation {
    // todo: add operationKey generation and verification
    @Override
    public Uni<Void> shutDown(String operationKey) {
        return Uni.createFrom().voidItem()
                .invoke(() -> Quarkus.asyncExit());
    }
}
