package ru.expanse.quader.controlpanel.service.impl;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import ru.expanse.quader.controlpanel.client.BotManagerClientAdapter;
import ru.expanse.quader.controlpanel.service.CommandService;

@ApplicationScoped
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {
    private final BotManagerClientAdapter botManagerClientAdapter;

    @Override
    public Uni<Void> shutDownBot(String botId) {
        return botManagerClientAdapter.shutDownBot(botId);
    }
}
