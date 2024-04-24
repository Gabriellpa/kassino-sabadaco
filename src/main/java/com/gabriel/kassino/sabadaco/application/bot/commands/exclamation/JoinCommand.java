package com.gabriel.kassino.sabadaco.application.bot.commands.exclamation;

import com.gabriel.kassino.sabadaco.application.player.GuildAudioManager;
import com.gabriel.kassino.sabadaco.application.player.LavaPlayerAudioProvider;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JoinCommand implements ExclamationCommand {

    public AudioPlayerManager playerManager;

    public JoinCommand(AudioPlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public String getName() {
        return "!bora";
    }

    @Override
    public Mono<Void> handle(MessageCreateEvent event) {
//        AudioPlayer player = playerManager.createPlayer();
        GuildAudioManager manager = GuildAudioManager.of(event.getGuildId().get(),playerManager );

        return  Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                // join returns a VoiceConnection which would be required if we were
                // adding disconnection features, but for now we are just ignoring it.
                // disconnection added!
                .flatMap(channel -> channel.join(spec -> spec.setProvider(manager.getProvider())))
                .then();
    }
}
