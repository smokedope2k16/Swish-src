package dev.angel.impl.command;

import dev.angel.Swish;
import dev.angel.api.command.Command;
import dev.angel.api.util.logging.Logger;

public class ClearPopsCommand extends Command {
    public ClearPopsCommand() {
        super(new String[]{"ClearPops", "clear"});
    }

    @Override
    public void run(String[] args) {
        Swish.getPopManager().getPopMap().clear();
        Logger.getLogger().log("Totem pops cleared");
    }
}
