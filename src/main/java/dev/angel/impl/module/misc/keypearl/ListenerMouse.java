package dev.angel.impl.module.misc.keypearl;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.asm.ducks.IClientPlayerInteractionManager;
import dev.angel.impl.events.MouseEvent;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

@SuppressWarnings("ConstantConditions")
public class ListenerMouse extends ModuleListener<KeyPearl, MouseEvent> {
    public ListenerMouse(KeyPearl module) {
        super(module, MouseEvent.class);
    }

    @Override
    public void call(MouseEvent event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (event.getKey() == 2 && mc.currentScreen == null) {
            int pearlSlot = ItemUtil.getHotbarItemSlot(Items.ENDER_PEARL);
            int lastSlot = mc.player.getInventory().selectedSlot;
            if (pearlSlot != -1) {
                InventoryUtil.swap(pearlSlot);
                ((IClientPlayerInteractionManager) mc.interactionManager).sendPacketWithSequence(mc.world, (sequence) -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, sequence));
                mc.player.swingHand(Hand.MAIN_HAND);
                InventoryUtil.swap(lastSlot);
            }
        }
    }
}
