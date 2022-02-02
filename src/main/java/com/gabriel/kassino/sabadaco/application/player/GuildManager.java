package com.gabriel.kassino.sabadaco.application.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GuildManager {

    private final AudioPlayerManager playerManager;

    public GuildManager(AudioPlayerManager audioPlayerManager) {
        this.playerManager = audioPlayerManager;

    }

    public synchronized GuildAudioManager getGuildAudioPlayer(Snowflake guildId) {
        return GuildAudioManager.of(guildId, playerManager);
    }

    public void loadAndPlay(MessageCreateEvent event, String trackUrl) {
        GuildAudioManager musicManager = getGuildAudioPlayer(event.getGuildId().orElse(null));

        if (!trackUrl.startsWith("http")) {
            trackUrl = "ytsearch: " + trackUrl;
        }
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResult(event, musicManager));
    }

    public Mono<Void> skup(MessageCreateEvent event) {
        GuildAudioManager musicManager = getGuildAudioPlayer(event.getGuildId().orElse(null));
        if (musicManager == null) {
            log.error("Nenhum MANAGER encontrado");
            return event.getMessage().getChannel()
                    .flatMap(messageChannel -> messageChannel.createMessage("Oops estou com algum PROBLEMINHA")).then();
        }
        return Mono.just(musicManager.getScheduler().nextOrSkipTrack())
                .then();
    }

    public Mono<Void> pouse(MessageCreateEvent event) {
        GuildAudioManager musicManager = getGuildAudioPlayer(event.getGuildId().orElse(null));
        return Mono.just(musicManager.getScheduler().pauseOrResumeTrack())
                .then();
    }

    // TODO: implementar
    // o que existia depende de uma lib nativa em C que abre a torneira da memoria e faz vazar uns 5 cantareira de memoria
    public void speedup(MessageCreateEvent event) {
//        GuildAudioManager musicManager = getGuildAudioPlayer(event.getGuildId().orElse(null));
//        musicManager.getPlayer().setFilterFactory((track, format, output)->{
//            TimescalePcmAudioFilter audioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
//            audioFilter.setSpeed(2F);
//            return Collections.singletonList(audioFilter);
//        });
    }

}
