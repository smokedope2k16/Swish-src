package dev.angel.impl.module.player.fastbreak;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.math.MathUtil;
import dev.angel.impl.events.UpdateEvent;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@SuppressWarnings("ConstantConditions")
public class ListenerUpdate extends ModuleListener<FastBreak, UpdateEvent> {
    public ListenerUpdate(FastBreak module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (!module.startTimer.passed(module.startDelay.getValue() * 250L)) {
            return;
        }

        if (module.pos == null) {
            return;
        }

        if (getDistanceSq(module.pos) > MathUtil.square(module.range.getValue())) {
            module.abortCurrentPos();
            return;
        }

        final Block block = mc.world.getBlockState(module.pos).getBlock();

        if (block instanceof AirBlock || block instanceof FluidBlock) {
            return;
        }

        int pickSlot = module.findPickaxeSlot();

        if (pickSlot == -1) {
            return;
        }

        if (module.mineTimer.passed(module.delay.getValue() * 100L)) {
            module.tryMine(pickSlot);
            module.mineTimer.reset();
        }
    }

    private double getDistanceSq(BlockPos pos) {
        final Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        return mc.player.squaredDistanceTo(vec);
    }
}
