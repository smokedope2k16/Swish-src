package dev.angel.impl.module.misc.deathcoordslog;

import dev.angel.api.event.bus.Listener;
import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.logging.Logger;
import dev.angel.api.util.text.TextColor;
import dev.angel.impl.events.ScreenEvent;
import net.minecraft.client.gui.screen.DeathScreen;

public class DeathCoordsLog extends Module {
    public DeathCoordsLog() {
        super("DeathCoordsLog", new String[]{"DeathCoordsLog", "DeathCoordsLogger", "DeathPos"}, "Logs your death position in chat", Category.MISC);
        this.offerListeners(new Listener<>(ScreenEvent.class) {
            @Override
            public void call(ScreenEvent event) {
                if (event.getScreen() instanceof DeathScreen && mc.player != null) {
                    String deathPosition;
                    deathPosition = String.format("X: %s, Y: %s, Z: %s", (int) mc.player.getX(), (int) mc.player.getY(), (int) mc.player.getZ());
                    Logger.getLogger().log(String.format("%sYou died at -> (%s)", TextColor.RED, deathPosition));
                }
            }
        });
    }
}
