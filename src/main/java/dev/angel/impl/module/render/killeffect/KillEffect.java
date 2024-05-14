package dev.angel.impl.module.render.killeffect;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;

public class KillEffect extends Module {
    public KillEffect() {
        super("KillEffect", new String[]{"KillEffect", "Effect", "Lightning"}, "Draws lightning on death positions", Category.RENDER);
        this.offerListeners(new ListenerDeath(this));
    }
}
