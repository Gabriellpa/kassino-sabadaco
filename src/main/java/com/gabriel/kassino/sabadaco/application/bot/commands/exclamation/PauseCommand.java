package com.gabriel.kassino.sabadaco.application.bot.commands.exclamation;

import com.gabriel.kassino.sabadaco.application.player.GuildManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PauseCommand implements ExclamationCommand{

    private final GuildManager guildManager;

    public PauseCommand(GuildManager guildManager){
        this.guildManager = guildManager;
    }

    @Override
    public String getName() {
        return "!pause";
    }

    @Override
    public Mono<Void> handle(MessageCreateEvent event) {
        return Mono.justOrEmpty(event.getMessage().getContent())
                .map(command -> guildManager.pouse(event))
                .then();
    }
}
