package dev.angel.impl.command;

import dev.angel.Swish;
import dev.angel.api.command.Command;
import dev.angel.api.module.Module;
import dev.angel.api.util.logging.Logger;
import dev.angel.api.value.util.KeyBind;

public class SetKeyBindCommand extends Command {
    public SetKeyBindCommand() {
        super(new String[]{"Bind", "SetKeyBind", "b"});
    }

    @Override
    public void run(String[] args) {
        final Module module = Swish.getModuleManager().getModuleByAlias(args[1]);

        if (module == null) {
            Logger.getLogger().log("That module does not exist");
            return;
        }

        int bind = KeyBind.getKeyIndex(args[2]);

        if (bind == 0) {
            Logger.getLogger().log("Unknown bind");
            return;
        }

        module.setBind(new KeyBind(bind).getKey());
        Logger.getLogger().log(String.format("Bound %s to %s", module.getLabel(), KeyBind.getKeyString(bind)));
    }
}
