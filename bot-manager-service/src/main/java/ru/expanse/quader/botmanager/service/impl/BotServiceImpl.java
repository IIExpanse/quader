package ru.expanse.quader.botmanager.service.impl;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import ru.expanse.quader.botmanager.client.BotClientAdapter;
import ru.expanse.quader.botmanager.resolver.BotDataResolver;
import ru.expanse.quader.botmanager.service.BotService;

@ApplicationScoped
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {
    private final BotClientAdapter botClientAdapter;
    private final BotDataResolver botDataResolver;

    @Override
    public Uni<Void> shutDownBot(String botId) {
        return botClientAdapter.shutDownBot(botDataResolver.resolve(botId).operationKey());
    }
}
