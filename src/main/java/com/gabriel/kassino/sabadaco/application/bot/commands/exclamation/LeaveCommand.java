package com.gabriel.kassino.sabadaco.application.bot.commands.exclamation;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class LeaveCommand implements  ExclamationCommand{

    @Override
    public Mono<Void> handle(MessageCreateEvent event) {

        return Mono.just(event.getMessage())
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(this.getCommandDisconnect(event)))
                .then();
    }

    @Override
    public String getName() {
        return "!tchau";
    }

    private String getCommandDisconnect(MessageCreateEvent event) {
        event.getClient().getVoiceConnectionRegistry().disconnect(event.getGuildId().get()).block();
        return  "Até a próxima";
    }
}
