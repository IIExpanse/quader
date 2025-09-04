package ru.expanse.quader.bot.service;

import io.smallrye.mutiny.Uni;

public interface ShutdownService {
    Uni<Void> shutDown(String operationKey);
}
