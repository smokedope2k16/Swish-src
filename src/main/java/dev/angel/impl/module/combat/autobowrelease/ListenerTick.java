package dev.angel.impl.module.combat.autobowrelease;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.api.util.network.PacketUtil;
import dev.angel.impl.events.TickEvent;
import net.minecraft.item.BowItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ListenerTick extends ModuleListener<AutoBowRelease, TickEvent> {
    public ListenerTick(AutoBowRelease module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.player != null && ItemUtil.isHolding(Items.BOW) && mc.options.useKey.isPressed() && mc.player.getItemUseTime() > module.bowCharge.getValue()) {
            PacketUtil.send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
            mc.player.stopUsingItem();
        }
    }
}
