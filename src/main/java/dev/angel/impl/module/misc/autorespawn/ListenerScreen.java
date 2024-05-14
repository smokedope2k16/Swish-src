package dev.angel.impl.module.misc.autorespawn;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.ScreenEvent;
import net.minecraft.client.gui.screen.DeathScreen;

public class ListenerScreen extends ModuleListener<AutoRespawn, ScreenEvent> {
    public ListenerScreen(AutoRespawn module) {
        super(module, ScreenEvent.class);
    }

    @Override
    public void call(ScreenEvent event) {
        if (mc.player != null && event.getScreen() instanceof DeathScreen) {
            event.setCanceled(true);
            mc.player.requestRespawn();
        }
    }
}
