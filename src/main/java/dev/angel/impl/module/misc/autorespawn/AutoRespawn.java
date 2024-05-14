package dev.angel.impl.module.misc.autorespawn;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn", new String[]{"AutoRespawn", "AntiDeathScreen", "Respawner"}, "Automatically respawns", Category.MISC);
        this.offerListeners(new ListenerScreen(this));
    }
}
