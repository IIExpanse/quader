package ru.expanse.quader.botmanager.service;

import io.smallrye.mutiny.Uni;

public interface BotService {
    Uni<Void> shutDownBot(String botId);
}
