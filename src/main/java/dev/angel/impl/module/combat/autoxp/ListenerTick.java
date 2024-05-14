package dev.angel.impl.module.combat.autoxp;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.impl.events.TickEvent;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class ListenerTick extends ModuleListener<AutoXP, TickEvent> {
    public ListenerTick(AutoXP module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        int expSlot = ItemUtil.findHotbarItem(Items.EXPERIENCE_BOTTLE);
        if (expSlot != -1 && module.expTimer.passed(module.delay.getValue() * 10) && ItemUtil.isHolding(Items.EXPERIENCE_BOTTLE)
                && mc.interactionManager != null && mc.player != null
                && !(mc.currentScreen instanceof InventoryScreen)) {
            if (module.swap.getValue()) {
                InventoryUtil.swap(expSlot);
            }
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            mc.player.swingHand(Hand.MAIN_HAND);
            module.expTimer.reset();
        }
    }
}
