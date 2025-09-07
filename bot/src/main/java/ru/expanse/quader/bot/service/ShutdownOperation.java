package ru.expanse.quader.bot.service;

import io.smallrye.mutiny.Uni;

public interface ShutdownOperation {
    Uni<Void> shutDown(String operationKey);
}
