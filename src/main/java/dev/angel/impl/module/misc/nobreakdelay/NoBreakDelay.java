package dev.angel.impl.module.misc.nobreakdelay;

import dev.angel.api.event.bus.Listener;
import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.asm.ducks.IClientPlayerInteractionManager;
import dev.angel.impl.events.TickEvent;

public class NoBreakDelay extends Module {
    public NoBreakDelay() {
        super("NoBreakDelay", new String[]{"NoBreakDelay", "NoMineDelay"}, "Removes mining delay", Category.MISC);
        this.offerListeners(new Listener<>(TickEvent.class) {
            @Override
            public void call(TickEvent event) {
                if (mc.player != null) {
                    final IClientPlayerInteractionManager controller = (IClientPlayerInteractionManager) mc.interactionManager;
                    if (controller != null) {
                        controller.setBlockHitDelay(0);
                    }
                }
            }
        });
    }
}
