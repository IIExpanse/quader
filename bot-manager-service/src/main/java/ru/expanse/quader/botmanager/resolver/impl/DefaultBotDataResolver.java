package ru.expanse.quader.botmanager.resolver.impl;

import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import ru.expanse.quader.botmanager.resolver.BotDataResolver;

@DefaultBean
@ApplicationScoped
public class DefaultBotDataResolver implements BotDataResolver {}
