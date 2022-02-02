package com.gabriel.kassino.sabadaco.application.bot.commands.slash;

import com.gabriel.kassino.sabadaco.application.bot.commands.Command;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public interface ChatInputInteractionCommand extends Command {
    Mono<Void> handle(ChatInputInteractionEvent event);
}
