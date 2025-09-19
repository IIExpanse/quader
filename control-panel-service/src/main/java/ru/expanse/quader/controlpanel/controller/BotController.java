package ru.expanse.quader.controlpanel.controller;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestPath;
import ru.expanse.quader.controlpanel.service.CommandService;

@Path("bot")
@RequiredArgsConstructor
public class BotController {
    private final CommandService commandService;

    @Path("shutdown/{botId}")
    @POST()
    @ResponseStatus(200)
    public Uni<Void> shutDownBot(@RestPath String botId) {
        return commandService.shutDownBot(botId);
    }
}
