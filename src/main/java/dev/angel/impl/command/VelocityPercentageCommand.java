package dev.angel.impl.command;

import dev.angel.Swish;
import dev.angel.api.command.Command;
import dev.angel.api.util.logging.Logger;
import dev.angel.impl.module.movement.velocity.Velocity;

public class VelocityPercentageCommand extends Command {
    public VelocityPercentageCommand() {
        super(new String[]{"VelocityPercentage", "Velo%", "VelocityPer"});
    }

    @Override
    public void run(String[] args) {
        if (args.length < 2) {
            Logger.getLogger().log("No percentage was entered");
            return;
        }

        final Velocity VELOCITY = Swish.getModuleManager().get(Velocity.class);
        int percent = Integer.parseInt(args[1]);
        VELOCITY.setVelocity(percent, percent);
        Logger.getLogger().log(String.format("Set horizontal & vertical velocity to -> %s", args[1]));
    }
}
