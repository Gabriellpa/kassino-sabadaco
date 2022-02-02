package com.gabriel.kassino.sabadaco.application.bot.events;

import com.gabriel.kassino.sabadaco.application.bot.commands.exclamation.ExclamationCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MessageListener {

    private final Map<String, ExclamationCommand> commands;

    public MessageListener(List<ExclamationCommand> exclamationCommandList) {
        commands = exclamationCommandList.stream()
                .collect(Collectors.toMap(ExclamationCommand::getName, Function.identity()));
    }

    public Mono<Void> processCommand(MessageCreateEvent eventMessage) {
        return Mono.just(eventMessage.getMessage())
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(message -> this.getCommandHandler(message.getContent()))
                .flatMap(command -> command.handle(eventMessage));

    }

    private Mono<ExclamationCommand> getCommandHandler(String message) {
        return Mono.just(message)
                .flatMap(this::getMessageBean);

    }

    private Mono<ExclamationCommand> getMessageBean(String content) {
        return  content.contains(" ") ?
                Mono.justOrEmpty(this.commands.get(content.split(" ")[0])) :
                Mono.justOrEmpty(this.commands.get(content));
    }
}
