package dev.angel.impl.module.misc.popcounter;

import dev.angel.Swish;
import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.logging.Logger;
import dev.angel.impl.events.DeathEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

public class ListenerDeath extends ModuleListener<PopCounter, DeathEvent> {
    public ListenerDeath(PopCounter module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        LivingEntity player = event.getEntity();
        if (player instanceof PlayerEntity) {
            String name = player.getName().getString();
            if (Swish.getPopManager().getPopMap().containsKey(name)) {
                boolean isSelf = player == mc.player;
                int pops = Swish.getPopManager().getPopMap().get(name);

                if (module.alternative()) {
                    Logger.getLogger().log(
                            Formatting.DARK_AQUA
                                    + (isSelf ? "You" : name)
                                    + Formatting.LIGHT_PURPLE
                                    + (isSelf ? " have died after popping your " : " has died after popping their ")
                                    + Formatting.DARK_PURPLE
                                    + pops + module.appendSuffix(pops)
                                    + Formatting.LIGHT_PURPLE
                                    + " totem.", -player.getId());
                } else {
                    Logger.getLogger().log((isSelf ? "You" : name)
                            + " died after popping "
                            + Formatting.GREEN
                            + pops
                            + Formatting.LIGHT_PURPLE
                            + (pops == 1 ? " totem!" : " totems!"), -player.getId());
                }
            }
        }
    }
}
