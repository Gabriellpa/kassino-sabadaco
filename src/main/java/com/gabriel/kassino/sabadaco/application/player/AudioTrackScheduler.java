package com.gabriel.kassino.sabadaco.application.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class AudioTrackScheduler extends AudioEventAdapter {

    private final BlockingQueue<AudioTrack> queue;
    private final AudioPlayer player;

    public AudioTrackScheduler(final AudioPlayer player) {
        this.queue = new LinkedBlockingQueue<>();
        this.player = player;
    }

    public void queue(AudioTrack track) {
        log.info("Tamanho da fila: {}",queue.size());
        log.info("Proxima musica: {}",queue.peek());
        if (!player.startTrack(track, true)) {
            log.info("Adicionado a fila");
            queue.add(track);
        } else {
            log.info("Iniciando track");
        }
    }

    public boolean nextOrSkipTrack() {
        return player.startTrack(queue.poll(), false);
    }

    public boolean pauseOrResumeTrack() {
        var p = player.getPlayingTrack();
        log.info("Track: {}", p);
        player.setPaused(!player.isPaused());
        return player.isPaused();
    }

    @Override
    public void onTrackEnd(final AudioPlayer player, final AudioTrack track, final AudioTrackEndReason endReason) {
        log.info("END REASON -> {}", endReason.name());
        if (endReason.mayStartNext) {
            log.info("Proxima track ou skup");
            nextOrSkipTrack();
        }else {
            log.info("Fim da track");
        }
    }
}
