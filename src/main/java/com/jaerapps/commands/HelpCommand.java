package com.jaerapps.commands;

import com.jaerapps.ResponseMessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class HelpCommand implements ICommand {
    @Override
    public MessageEmbed runCommand(SlashCommandInteractionEvent event) {
        return ResponseMessageBuilder.buildMultiFieldStandardResponse(
                List.of(
                        new ResponseMessageBuilder.MessageEmbedField(
                                "Overview",
                                "This bot runs entirely on slash commands.  " +
                                        "Simply type a forward slash (/) in Discord to see options and parameters for each command listed here.\n" +
                                        "Ping LaDfBC#1246 for help, suggestions, or to complain."
                        )
//                        new ResponseMessageBuilder.MessageEmbedField(
//                                "guess <REQUIRED: guess>",
//                                "This will add your guess to the currently active play. " +
//                                        "I will give you a thumbs up if everything worked!"
//                        )
                )
        );
    }
}
