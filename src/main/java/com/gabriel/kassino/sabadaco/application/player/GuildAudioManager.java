package com.gabriel.kassino.sabadaco.application.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.common.util.Snowflake;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GuildAudioManager {

    @Getter
    private final AudioPlayer player;
    @Getter
    private final AudioTrackScheduler scheduler;
    @Getter
    private final LavaPlayerAudioProvider provider;

    private static final Map<Snowflake, GuildAudioManager> MANAGERS = new ConcurrentHashMap<>();

    public static GuildAudioManager of(final Snowflake id, AudioPlayerManager audioPlayerManager) {
        return MANAGERS.computeIfAbsent(id, ignored -> new GuildAudioManager(audioPlayerManager));
    }

    private GuildAudioManager(AudioPlayerManager audioPlayerManager) {
        player = audioPlayerManager.createPlayer();
        scheduler = new AudioTrackScheduler(player);
        provider = new LavaPlayerAudioProvider(player);
        player.addListener(scheduler);
    }

}
