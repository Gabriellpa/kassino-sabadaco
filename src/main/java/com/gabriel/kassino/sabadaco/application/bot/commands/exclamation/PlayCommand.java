package com.gabriel.kassino.sabadaco.application.bot.commands.exclamation;

import com.gabriel.kassino.sabadaco.application.player.GuildManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
public class PlayCommand implements ExclamationCommand {

    private GuildManager guildManager;

    public PlayCommand(GuildManager guildManager) {
        this.guildManager = guildManager;
    }

    @Override
    public String getName() {
        return "!kassino";
    }

    @Override
    public Mono<Void> handle(MessageCreateEvent event) {
//        AudioPlayer player = playerManager.createPlayer();
//        TrackScheduler trackScheduler = new TrackScheduler(player);
//        player.addListener(trackScheduler);
        return Mono.justOrEmpty(event.getMessage().getContent())
                .doOnNext(command -> guildManager.loadAndPlay(event ,command.replace("!kassino", "").trim()))
                .then();
    }
}
