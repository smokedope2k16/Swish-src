package dev.angel.impl.module.combat.blocker;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.block.HoleUtil;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.impl.events.PacketEvent;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.util.math.BlockPos;

public class PacketListener extends ModuleListener<Blocker, PacketEvent.Receive<BlockBreakingProgressS2CPacket>> {
    public PacketListener(Blocker module) {
        super(module, PacketEvent.Receive.class, BlockBreakingProgressS2CPacket.class);
    }

    @Override
    public void call(PacketEvent.Receive<BlockBreakingProgressS2CPacket> event) {
        if (mc.player == null) {
            return;
        }

        BlockBreakingProgressS2CPacket packet = event.getPacket();
        BlockPos pos = packet.getPos();

        if (module.isSurroundBlock(pos) && HoleUtil.isSafeBlock(pos)) {
            int slot = ItemUtil.findHotbarItem(Items.OBSIDIAN);
            if (slot == -1) {
                return;
            }

            int oldSlot = mc.player.getInventory().selectedSlot;
            InventoryUtil.swap(slot);
        }
    }
}
