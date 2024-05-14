package dev.angel.impl.command;

import dev.angel.Swish;
import dev.angel.api.command.Command;
import dev.angel.api.util.logging.Logger;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super(new String[]{"Config", "configs", "con"});
    }

    @Override
    public void run(String[] args) {
        switch (args[1].toUpperCase()) {
            case "SAVE" ->  {
                Swish.getConfigManager().saveConfig();
                Swish.getFriendManager().saveFriends();
                Logger.getLogger().log("Config saved successfully");
            }
            case "LOAD" -> {
                Swish.getConfigManager().loadConfig();
                Swish.getFriendManager().loadFriends();
                Logger.getLogger().log("Config loaded successfully");
            }
        }
    }
}
