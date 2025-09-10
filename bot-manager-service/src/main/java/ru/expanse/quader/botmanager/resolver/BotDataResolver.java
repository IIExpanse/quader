package ru.expanse.quader.botmanager.resolver;

import ru.expanse.quader.botmanager.schema.BotData;

public interface BotDataResolver {
    // todo: remove default implementation, implement custom NameResolverProvider
    default BotData resolve(String botId) {
        return new BotData("localhost", "");
    }
}
