package com.gabriel.kassino.sabadaco.application.bot.events;

import com.gabriel.kassino.sabadaco.application.bot.commands.exclamation.ExclamationCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MessageCreateListener extends MessageListener implements EventListener<MessageCreateEvent> {

    public MessageCreateListener(List<ExclamationCommand> exclamationCommandList) {
        super(exclamationCommandList);
    }

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return processCommand(event).then();
    }

}