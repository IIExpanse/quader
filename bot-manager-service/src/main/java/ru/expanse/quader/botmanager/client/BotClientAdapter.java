package ru.expanse.quader.botmanager.client;

import io.smallrye.mutiny.Uni;

public interface BotClientAdapter {
    Uni<Void> shutDownBot(String operationKey);
}
