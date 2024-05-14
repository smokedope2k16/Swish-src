package dev.angel.impl.command;

import dev.angel.Swish;
import dev.angel.api.command.Command;
import dev.angel.api.util.logging.Logger;
import net.minecraft.util.Util;

public class OpenFolderCommand extends Command {
    public OpenFolderCommand() {
        super(new String[]{"OpenFolder", "folder"});
    }

    @Override
    public void run(String[] args) {
        Util.getOperatingSystem().open(Swish.DIRECTORY.toURI());
        Logger.getLogger().log("Opened the swish folder");
    }
}
