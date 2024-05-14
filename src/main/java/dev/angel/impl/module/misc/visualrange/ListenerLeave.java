package dev.angel.impl.module.misc.visualrange;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.logging.Logger;
import dev.angel.impl.events.EntityWorldEvent;
import net.minecraft.entity.player.PlayerEntity;

@SuppressWarnings("ConstantConditions")
public class ListenerLeave extends ModuleListener<VisualRange, EntityWorldEvent.Remove> {
    public ListenerLeave(VisualRange module) {
        super(module, EntityWorldEvent.Remove.class);
    }

    @Override
    public void call(EntityWorldEvent.Remove event) {
        if (module.left.getValue() && event.getEntity() instanceof PlayerEntity player && !player.getName().getString().equals(mc.player.getName().getString())) {
            final String name = player.getName().getString();
            Logger.getLogger().log(String.format("%s has left your visual range", name), player.getId());
        }
    }
}
