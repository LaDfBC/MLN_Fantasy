package com.jaerapps.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface ICommand {
    public MessageEmbed runCommand(SlashCommandInteractionEvent event);
}
