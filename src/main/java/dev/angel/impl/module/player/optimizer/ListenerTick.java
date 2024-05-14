package dev.angel.impl.module.player.optimizer;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.block.BlockUtil;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.impl.events.TickEvent;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class ListenerTick extends ModuleListener<Optimizer, TickEvent> {
    public ListenerTick(Optimizer module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), mc.options.useKey.getDefaultKey().getCode()) && mc.interactionManager != null && mc.player != null && ItemUtil.isHolding(Items.END_CRYSTAL)) {
            if (mc.crosshairTarget instanceof BlockHitResult hit) {
                final BlockPos block = hit.getBlockPos();
                if (BlockUtil.canPlaceCrystal(block)) {
                    final ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                    if (result == ActionResult.SUCCESS) {
                        mc.player.swingHand(Hand.MAIN_HAND);
                    }
                }
            }
        }
    }
}
