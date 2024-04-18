package com.jaerapps.util;

import com.google.common.collect.Lists;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.List;

public class MessageResponder {
    private final List<MessageEmbed> queuedMessages;
    private final MessageChannel channel;

    private MessageResponder(MessageChannel channel) {
        queuedMessages = Lists.newArrayList();
        this.channel = channel;
    }

    public List<MessageEmbed> getMessages() {
        return queuedMessages;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public static MessageResponder create(MessageChannel channel) {
        return new MessageResponder(channel);
    }


    public MessageResponder addMessage(MessageEmbed message) {
        this.queuedMessages.add(message);
        return this;
    }

    public void sendResponseMessages(SlashCommandInteractionEvent event) {
        event
                .getHook()
                .sendMessageEmbeds(queuedMessages)
                .queue();
    }
}