package ru.expanse.quader.bot.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.log4j.Log4j2;

@ApplicationScoped
@Log4j2
public class ShutdownOperationImpl implements ShutdownOperation {
    // todo: add operationKey generation and verification
    @Override
    public Uni<Void> shutDown(String operationKey) {
        return Uni.createFrom().voidItem()
                .invoke(() -> log.info("Invoking shutdown command.."));
//                .invoke(() -> Quarkus.asyncExit());
    }
}
