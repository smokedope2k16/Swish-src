package dev.angel.impl.command;

import dev.angel.Swish;
import dev.angel.api.command.Command;
import dev.angel.api.util.logging.Logger;
import dev.angel.api.util.text.TextColor;
import net.minecraft.util.Formatting;

public class FriendCommand extends Command {
    public FriendCommand() {
        super(new String[]{"Friend", "f", "frien", "amigo"});
    }

    @Override
    public void run(String[] args) {
        if (args.length < 3) {
            Logger.getLogger().log("Command usage: add/del, name");
            return;
        }

        final String argument = args[1];
        final String name = args[2];

        switch (argument.toUpperCase()) {
            case ("ADD") -> {
                if (Swish.getFriendManager().isFriend(name)) {
                    Logger.getLogger().log(String.format("%s is already a friend", name));
                    return;
                }
                Swish.getFriendManager().addFriend(name);
                Logger.getLogger().log(String.format("Added %s%s%s as a friend", Formatting.BLUE, name, Formatting.LIGHT_PURPLE));
            }
            case ("DEL"), ("DELETE"), ("REM"), ("REMOVE") -> {
                if (!Swish.getFriendManager().isFriend(name)) {
                    Logger.getLogger().log("That user is not a friend");
                    return;
                }
                Swish.getFriendManager().removeFriend(name);
                Logger.getLogger().log(String.format("Removed %s%s%s as a friend", TextColor.RED, name, TextColor.LIGHT_PURPLE));
            }
        }
    }
}
