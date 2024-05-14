package dev.angel.impl.command;

import dev.angel.Swish;
import dev.angel.api.command.Command;
import dev.angel.api.util.logging.Logger;

import java.util.StringJoiner;

public class HelpCommand extends Command {
    public HelpCommand() {
        super(new String[]{"help", "h"});
    }

    @Override
    public void run(String[] args) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        Swish.getCommandManager().getCommands().forEach(module -> stringJoiner.add(module.getAlias()[0]));
        Logger.getLogger().log(String.format("List of commands you can use: %s", stringJoiner));
    }
}
