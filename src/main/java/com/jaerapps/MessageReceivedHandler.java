package com.jaerapps;

import com.google.inject.Inject;
import com.jaerapps.commands.*;
import com.jaerapps.enums.CoreCommand;
import com.jaerapps.util.MessageResponder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class MessageReceivedHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(MessageReceivedHandler.class);

    private final AddPlayerCommand addPlayerCommand;
//    private final SetSeasonCommand setSeasonCommand;
//    private final SetSessionCommand setSessionCommand;
//    private final GuessCommand guessCommand;
//    private final ResolveCommand resolveCommand;
//    private final PointsCommand pointsCommand;
//    private final ExtractDataCommand extractDataCommand;

    @Inject
    public MessageReceivedHandler(
            @Nonnull final AddPlayerCommand addPlayerCommand
    ) {
        this.addPlayerCommand = addPlayerCommand;
    }

    public MessageResponder handleMessage(SlashCommandInteractionEvent event) {
        MessageResponder responder = MessageResponder.create(event.getChannel());
        CoreCommand incomingCommand = CoreCommand.init(event.getName());

        switch (incomingCommand) {
            case HELP:
                responder.addMessage(new HelpCommand().runCommand(event));
                break;
            case ADD_PLAYER:
                responder.addMessage(addPlayerCommand.runCommand(event));
                break;
            default:
                responder.addMessage(ResponseMessageBuilder.buildErrorResponse(
                        "Unrecognized command: " +
                                event.getName() +
                                ". Please use /help to see available commands")
                );
                break;

        }
        return responder;
    }



}
