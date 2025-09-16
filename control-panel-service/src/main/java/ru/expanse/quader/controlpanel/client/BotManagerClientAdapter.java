package ru.expanse.quader.controlpanel.client;

import io.smallrye.mutiny.Uni;

public interface BotManagerClientAdapter {
    Uni<Void> shutDownBot(String botId);
}
