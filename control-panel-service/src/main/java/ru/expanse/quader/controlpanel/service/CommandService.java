package ru.expanse.quader.controlpanel.service;

import io.smallrye.mutiny.Uni;

public interface CommandService {
    Uni<Void> shutDownBot(String botId);
}
