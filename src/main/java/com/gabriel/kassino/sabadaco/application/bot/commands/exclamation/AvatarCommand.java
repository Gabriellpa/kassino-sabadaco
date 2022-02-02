package com.gabriel.kassino.sabadaco.application.bot.commands.exclamation;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AvatarCommand implements ExclamationCommand {
    @Override
    public String getName() {
        return "!avatar";
    }

    @Override
    public Mono<Void> handle(MessageCreateEvent event) {
        return this.getAvatarURL(event.getMessage())
                .zipWith(event.getMessage().getChannel())
                .flatMap(tuple -> {
                    var url = tuple.getT1();
                    var channel = tuple.getT2();
                    return channel.createMessage(url);
                }).then();
    }

    private Mono<String> getAvatarURL(Message message) {
        var optionalUser = message.getAuthor();
        var optionalSnowflake = message.getUserMentionIds().stream().findFirst();
        if(optionalUser.isEmpty() || optionalSnowflake.isEmpty()) {
            return Mono.just("Something wrong");
        }
        return Mono.just(getSnkowflakeId(optionalUser.get(), optionalSnowflake.get()))
                .flatMap(snowflake -> {
                    if(snowflake.equals(optionalUser.get().getId())) {
                        return Mono.just(String.format("%s?size=512",optionalUser.get().getAvatarUrl()));
                    }
                    return message.getGuild()
                            .flatMap(guild -> guild.getMemberById(snowflake))
                            .flatMap(member -> Mono.just(String.format("%s?size=512",member.getAvatarUrl())));
                });
    }

    private Snowflake getSnkowflakeId(User user, Snowflake  snowflakeId){
        return snowflakeId == null? user.getId() : snowflakeId;
    }
}
