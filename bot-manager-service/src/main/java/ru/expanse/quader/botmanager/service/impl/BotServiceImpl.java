package ru.expanse.quader.botmanager.service.impl;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.expanse.quader.botmanager.client.BotClientAdapter;
import ru.expanse.quader.botmanager.resolver.BotDataResolver;
import ru.expanse.quader.botmanager.service.BotService;

@ApplicationScoped
@RequiredArgsConstructor
@Log4j2
public class BotServiceImpl implements BotService {
    private final BotClientAdapter botClientAdapter;
    private final BotDataResolver botDataResolver;

    @Override
    public Uni<Void> shutDownBot(String botId) {
        return Uni.createFrom().nullItem()
                .invoke(() -> log.trace("Starting resolving operationKey for botId={}", botId))
                .map(empty -> botDataResolver.resolve(botId).operationKey())
                .invoke(operationKey -> log.trace("Resolved botId={} into operationKey={}", botId, operationKey))
                .flatMap(botClientAdapter::shutDownBot);
    }
}
