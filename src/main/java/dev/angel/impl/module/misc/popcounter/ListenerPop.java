package dev.angel.impl.module.misc.popcounter;

import dev.angel.Swish;
import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.logging.Logger;
import dev.angel.impl.events.PopEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

public class ListenerPop extends ModuleListener<PopCounter, PopEvent> {
    public ListenerPop(PopCounter module) {
        super(module, PopEvent.class);
    }

    @Override
    public void call(PopEvent event) {
        PlayerEntity entity = event.getPlayer();
        String name = entity.getName().getString();
        boolean isSelf = entity == mc.player;
        int pops = Swish.getPopManager().getPopMap().get(name);

        if (module.alternative()) {
            Logger.getLogger().log(
                    Formatting.DARK_AQUA
                            + (isSelf ? "You" : name)
                            + Formatting.LIGHT_PURPLE
                            + (isSelf ? " have popped your " : " has popped their ")
                            + Formatting.DARK_PURPLE
                            + pops + module.appendSuffix(pops)
                            + Formatting.LIGHT_PURPLE
                            + " totem.", -event.getPlayer().getId());
        } else {
            Logger.getLogger().log((isSelf ? "You" : name)
                    + (isSelf ? " popped " : " has popped ")
                    + Formatting.RED
                    + pops
                    + Formatting.LIGHT_PURPLE
                    + (pops == 1 ? " time in total!" : " times in total!"), -entity.getId());
        }
    }
}
