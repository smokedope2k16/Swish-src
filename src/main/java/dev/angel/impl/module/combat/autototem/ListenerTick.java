package dev.angel.impl.module.combat.autototem;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.impl.events.TickEvent;
import dev.angel.impl.module.combat.autototem.mode.AutoTotemMode;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;

public class ListenerTick extends ModuleListener<AutoTotem, TickEvent> {
    public ListenerTick(AutoTotem module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.world == null || mc.player == null) {
            return;
        }

        if (mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
            module.swapTimer.reset();
            module.equipTimer.reset();
            module.inventoryTimer.reset();
            return;
        }

        int slot = ItemUtil.getInventoryItemSlot(Items.TOTEM_OF_UNDYING);
        if (slot == -1) {
            return;
        }

        if (module.mode.getValue() == AutoTotemMode.BLATANT) {
            InventoryUtil.moveToOffhand(slot);
            return;
        }

        int hotbarSlot = ItemUtil.getHotbarItemSlot(Items.TOTEM_OF_UNDYING);

        if (hotbarSlot == -1) {
            if (module.openInventory.getValue() && module.inventoryTimer.passed(module.invDelay.getValue() * 100)) {
                mc.setScreen(new InventoryScreen(mc.player));
                module.inventoryTimer.reset();
            }

            if (module.autoEquip.getValue()) {
                if (module.equipTimer.passed(module.equipDelay.getValue() * 100)) {
                    InventoryUtil.moveToOffhand(slot);
                    module.equipTimer.reset();
                    module.refillTimer.reset();
                }
            }

            if (module.refill.getValue() && module.refillTimer.passed(300)) {
                int newSlot = ItemUtil.getInventoryItemSlot(Items.TOTEM_OF_UNDYING);
                if (newSlot == -1) {
                    return;
                }
                InventoryUtil.moveItem(newSlot, module.refillSlot.getValue());
                module.refillTimer.reset();
            }
            return;
        }

        if (module.autoSwap.getValue()) {
            if (module.swapTimer.passed(module.swapDelay.getValue() * 100)) {
                InventoryUtil.swap(hotbarSlot);
                if (module.autoEquip.getValue()) {
                    if (module.equipTimer.passed(module.equipDelay.getValue() * 100)) {
                        InventoryUtil.moveToOffhand(hotbarSlot);
                    }
                }
            }
        }
    }
}
