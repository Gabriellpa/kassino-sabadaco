package com.gabriel.kassino.sabadaco.application.bot.commands.exclamation;

import com.gabriel.kassino.sabadaco.application.bot.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public interface ExclamationCommand extends Command {

    Mono<Void> handle(MessageCreateEvent event);
}
