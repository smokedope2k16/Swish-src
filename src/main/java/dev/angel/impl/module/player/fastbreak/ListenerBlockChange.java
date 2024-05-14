package dev.angel.impl.module.player.fastbreak;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.network.PacketUtil;
import dev.angel.impl.events.PacketEvent;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.math.Direction;

@SuppressWarnings("ConstantConditions")
public class ListenerBlockChange extends ModuleListener<FastBreak, PacketEvent.Receive<BlockUpdateS2CPacket>> {
    public ListenerBlockChange(FastBreak module) {
        super(module, PacketEvent.Receive.class, BlockUpdateS2CPacket.class);
    }

    @Override
    public void call(PacketEvent.Receive<BlockUpdateS2CPacket> event) {
        BlockUpdateS2CPacket packet = event.getPacket();

        if (packet.getPos().equals(module.pos) && packet.getState()== mc.world.getBlockState(module.pos) && module.abortPacket) {
            PacketUtil.send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, module.pos, Direction.DOWN));
            module.abortPacket = false;
        }
    }
}
