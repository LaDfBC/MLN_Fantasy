package com.jaerapps.enums;

public enum CoreCommand {
    HELP("help"),
    DRAFT("draft"),
    DROP_PLAYER("drop-player"),
    ADD_PLAYER("add-player"),
    TRADE("trade"),
    CHANGE_MANAGER("change-manager"),
    SCOREBOARD("scoreboard"),
    SET_LINEUP("set-lineup"),
    SHOW_TEAM("show-team");

    private final String commandName;

    CoreCommand(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    /**
     * Loads an instance of this enum using the commandName String by exact match only.
     *
     * @param incomingCommand - The command sent by the user, telling the bot what to do.
     * @return The command found to match
     * @throws IllegalArgumentException if no match can be found.
     */
    public static CoreCommand init(String incomingCommand) {
        for (CoreCommand currentCommand : CoreCommand.values()) {
            if (incomingCommand.equals(currentCommand.commandName)) {
                return currentCommand;
            }
        }

        throw new IllegalArgumentException("Unrecognized command: " + incomingCommand);
    }
}
