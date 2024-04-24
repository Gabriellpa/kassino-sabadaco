package com.gabriel.kassino.sabadaco.application.player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.VoiceChannelJoinSpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AudioLoadResult implements AudioLoadResultHandler {

    private final MessageCreateEvent event;
    private final GuildAudioManager musicManager;

    public AudioLoadResult(MessageCreateEvent event, GuildAudioManager musicManager) {
        this.event = event;
        this.musicManager = musicManager;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        sendMessageToChannel("Foi pra fila: " + track.getInfo().title);
        play(event.getGuild().block(), musicManager, track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        AudioTrack firstTrack = playlist.getSelectedTrack();

        if (firstTrack == null) {
            firstTrack = playlist.getTracks().get(0);
        }

        sendMessageToChannel("Tocando essa caraelha " + firstTrack.getInfo().title + " (" + playlist.getName() + ")");
        play(event.getGuild().block(), musicManager, firstTrack);
    }

    @Override
    public void noMatches() {
        sendMessageToChannel("Não achei isso aí meu");
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        log.info("failed -> {}", exception.getMessage());
        sendMessageToChannel("Bah, não rolou: " + exception.getMessage());
    }

    private void play(Guild guild, GuildAudioManager musicManager, AudioTrack track) {
        attachToFirstVoiceChannel(guild, musicManager.getProvider());
        musicManager.getScheduler().queue(track);
    }

    private static void attachToFirstVoiceChannel(Guild guild, LavaPlayerAudioProvider provider) {
        VoiceChannel voiceChannel = guild.getChannels().ofType(VoiceChannel.class).blockFirst();
        var inVoiceChannel = guild.getVoiceStates()
                .any(voiceState -> guild.getClient().getSelfId().equals(voiceState.getUserId()))
                .block();

        if (Boolean.FALSE.equals(inVoiceChannel)) {
            assert voiceChannel != null;
            voiceChannel.join(VoiceChannelJoinSpec.builder().provider(provider).build())
                    .doOnError(e -> System.out.println("Erro ao se conectar ao canal" + e.getMessage())).block();
        }
    }

    private void sendMessageToChannel(String message) {
        this.event.getMessage().getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(message)).block();
    }
}
