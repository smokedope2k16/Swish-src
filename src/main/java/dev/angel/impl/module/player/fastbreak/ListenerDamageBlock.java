package dev.angel.impl.module.player.fastbreak;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.network.PacketUtil;
import dev.angel.impl.events.DamageBlockEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@SuppressWarnings("ConstantConditions")
public class ListenerDamageBlock extends ModuleListener<FastBreak, DamageBlockEvent> {
    public ListenerDamageBlock(FastBreak module) {
        super(module, DamageBlockEvent.class);
    }

    @Override
    public void call(DamageBlockEvent event) {
        final BlockPos pos = event.getPos();
        final Direction direction = event.getDirection();

        if (!module.timer.passed(250L) || module.findPickaxeSlot() == -1) {
            return;
        }

        if (canBreak(pos)) {
            boolean aborted = false;
            if (module.pos != null && !module.pos.equals(event.getPos())) {
                module.abortCurrentPos();
                aborted = true;
            }

            if (!aborted && module.pos != null && module.pos.equals(event.getPos())) {
                module.abortCurrentPos();
                module.timer.reset();
                return;
            }

            if (module.pos == null) {
                setPos(pos, direction);
                mc.player.swingHand(Hand.MAIN_HAND);
                PacketUtil.send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction));
            }

            module.abortPacket = true;

            event.setCanceled(true);
            module.timer.reset();
        }
    }

    private void setPos(BlockPos pos, Direction direction) {
        module.reset();
        module.pos = pos;
        module.direction = direction;
    }

    private boolean canBreak(BlockPos pos) {
        final BlockState blockState = mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();

        return block.getHardness() != -1;
    }
}
