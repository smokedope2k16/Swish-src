package dev.angel.impl.module.misc.keypearl;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;

public class KeyPearl extends Module {

    public KeyPearl() {
        super("KeyPearl", new String[]{"KeyPearl", "AutoPearl", "ThrowPearl"}, "Throws a pearl automatically", Category.MISC);
        this.offerListeners(new ListenerMouse(this));
    }

}
