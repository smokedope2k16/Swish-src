package dev.angel.impl.module.misc.autorekit;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.impl.events.TickEvent;
import net.minecraft.item.Items;

public class ListenerTick extends ModuleListener<AutoRekit, TickEvent> {
    public ListenerTick(AutoRekit module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (InventoryUtil.count(Items.TOTEM_OF_UNDYING) < module.totemCount.getValue() || InventoryUtil.count(Items.TOTEM_OF_UNDYING) == 0) {
            if (mc.player != null) {
                if (module.sentTimer.passed(600)) {
                    switch (module.mode.getValue()) {
                        case K, KIT ->
                                mc.player.networkHandler.sendChatCommand(module.mode.getValue().getKitName() + module.kit.getValue().toString());
                        case LOAD ->
                                mc.player.networkHandler.sendChatCommand(module.mode.getValue().getKitName() + module.kitName.getValue());
                    }
                    module.sentTimer.reset();
                }
            }
        }
    }
}
