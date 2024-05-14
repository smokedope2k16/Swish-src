package dev.angel.impl.module.movement.inventorymove;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.TickEvent;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class ListenerTick extends ModuleListener<InventoryMove, TickEvent> {
    public ListenerTick(InventoryMove module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen)) {
            KeyBinding.setKeyPressed(mc.options.forwardKey.getDefaultKey(), InputUtil.isKeyPressed(mc.getWindow().getHandle(), mc.options.forwardKey.getDefaultKey().getCode()));
            KeyBinding.setKeyPressed(mc.options.backKey.getDefaultKey(), InputUtil.isKeyPressed(mc.getWindow().getHandle(), mc.options.backKey.getDefaultKey().getCode()));
            KeyBinding.setKeyPressed(mc.options.leftKey.getDefaultKey(), InputUtil.isKeyPressed(mc.getWindow().getHandle(), mc.options.leftKey.getDefaultKey().getCode()));
            KeyBinding.setKeyPressed(mc.options.rightKey.getDefaultKey(), InputUtil.isKeyPressed(mc.getWindow().getHandle(), mc.options.rightKey.getDefaultKey().getCode()));
            if (module.jump.getValue()) {
                KeyBinding.setKeyPressed(mc.options.jumpKey.getDefaultKey(), InputUtil.isKeyPressed(mc.getWindow().getHandle(), mc.options.jumpKey.getDefaultKey().getCode()));
            }
            if (module.sprint.getValue()) {
                KeyBinding.setKeyPressed(mc.options.sprintKey.getDefaultKey(), InputUtil.isKeyPressed(mc.getWindow().getHandle(), mc.options.sprintKey.getDefaultKey().getCode()));
            }
            if (module.sneak.getValue()) {
                KeyBinding.setKeyPressed(mc.options.sneakKey.getDefaultKey(), InputUtil.isKeyPressed(mc.getWindow().getHandle(), mc.options.sneakKey.getDefaultKey().getCode()));
            }
        }
    }
}