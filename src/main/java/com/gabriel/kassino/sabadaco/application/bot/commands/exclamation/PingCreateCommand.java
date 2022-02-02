package com.gabriel.kassino.sabadaco.application.bot.commands.exclamation;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class PingCreateCommand implements ExclamationCommand {

    @Override
    public Mono<Void> handle(MessageCreateEvent event) {

        return Mono.just(event.getMessage())
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(this.getCommandMessage(event)))
                .then();
    }

    @Override
    public String getName() {
        return "!ping";
    }

    private String getCommandMessage(MessageCreateEvent event) {
        var optionalNickname = event.getMember().flatMap(Member::getNickname);
       return  String.format("Pong para %s", optionalNickname.orElse("No nickname :(~"));
    }
}
