package ru.expanse.quader.controlpanel.controller;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestPath;
import ru.expanse.quader.controlpanel.service.CommandService;

@Path("bot")
@RequiredArgsConstructor
@Log4j2
public class BotController {
    private final CommandService commandService;

    @Path("shutdown/{botId}")
    @POST()
    @ResponseStatus(200)
    public Uni<Void> shutDownBot(@RestPath String botId) {
        return Uni.createFrom().nullItem()
                .invoke(() -> log.info("Received shutdown request for bot with id={}", botId))
                .flatMap(empty -> commandService.shutDownBot(botId))
                .invoke(() -> log.info("Finished processing shutdown request for bot with id={}", botId));
    }
}
