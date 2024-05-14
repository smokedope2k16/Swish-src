package dev.angel.impl.module.render.pearltrace;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;

public class PearlTrace extends Module {
    public PearlTrace() {
        super("PearlTrace", new String[]{"pearltrace", "trails", "pearltrajectory"}, "Draws the trajectory of pearls", Category.RENDER);
    }
}
