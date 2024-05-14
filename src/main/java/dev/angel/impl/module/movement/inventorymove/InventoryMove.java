package dev.angel.impl.module.movement.inventorymove;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.Value;
import net.minecraft.client.option.KeyBinding;

public class InventoryMove extends Module {
    protected final Value<Boolean> sprint = new Value<>(
            new String[]{"Sprint", "Sprinting"},
            "Whitelists sprint key",
            true
    );

    protected final Value<Boolean> sneak = new Value<>(
            new String[]{"Sneak", "Crouch"},
            "Whitelists sneak key",
            true
    );

    protected final Value<Boolean> jump = new Value<>(
            new String[]{"Jump", "Jumping"},
            "Whitelists jump key",
            true
    );

    public InventoryMove() {
        super("InventoryMove", new  String[]{"InventoryMove", "invmove", "moveinv"}, "Allows for moving inside inventory screens", Category.MOVEMENT);
        this.offerValues(sprint, sneak, jump);
        this.offerListeners(new ListenerTick(this));
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen != null) {
            KeyBinding.setKeyPressed(mc.options.forwardKey.getDefaultKey(), false);
            KeyBinding.setKeyPressed(mc.options.backKey.getDefaultKey(), false);
            KeyBinding.setKeyPressed(mc.options.leftKey.getDefaultKey(), false);
            KeyBinding.setKeyPressed(mc.options.rightKey.getDefaultKey(), false);
            if (jump.getValue()) {
                KeyBinding.setKeyPressed(mc.options.jumpKey.getDefaultKey(), false);
            }
            if (sprint.getValue()) {
                KeyBinding.setKeyPressed(mc.options.sprintKey.getDefaultKey(), false);
            }
            if (sneak.getValue()) {
                KeyBinding.setKeyPressed(mc.options.sneakKey.getDefaultKey(), false);
            }
        }
    }
}
