package dev.angel.api.util.block;

import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.util.math.PositionUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class BlockUtil implements Minecraftable {

    public static List<BlockPos> getSphere(Entity entity, float radius, boolean ignoreAir) {
        final List<BlockPos> sphere = new ArrayList<>();

        final BlockPos pos = PositionUtil.getPosition(entity);

        final int posX = pos.getX();
        final int posY = pos.getY();
        final int posZ = pos.getZ();

        final int radiuss = (int) radius;

        for (int x = posX - radiuss; x <= posX + radius; x++) {
            for (int z = posZ - radiuss; z <= posZ + radius; z++) {
                for (int y = posY - radiuss; y < posY + radius; y++) {
                    if ((posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y) < radius * radius) {
                        final BlockPos position = new BlockPos(x, y, z);
                        if (ignoreAir && mc.world.getBlockState(position).getBlock() == Blocks.AIR) {
                            continue;
                        }
                        sphere.add(position);
                    }
                }
            }
        }

        return sphere;
    }

    public static Direction getFacing(BlockPos pos) {
        return getFacing(pos, mc.world);
    }

    public static Direction getFacing(BlockPos pos, ClientWorld provider) {
        for (Direction facing : Direction.values()) {
            if (!provider.getBlockState(pos.offset(facing)).isAir()) {
                return facing;
            }
        }

        return null;
    }


    public static boolean canPlaceCrystal(BlockPos block) {
        if (mc.world != null) {
            BlockState blockState = mc.world.getBlockState(block);

            if (!blockState.isOf(Blocks.OBSIDIAN) && !blockState.isOf(Blocks.BEDROCK)) {
                return false;
            }

            for (Entity entity : mc.world.getOtherEntities(null, new Box(block.up()))) {
                if (!entity.isAlive() || entity instanceof EndCrystalEntity) {
                    continue;
                }

                return false;
            }
            return mc.world.isAir(block.up());
        }
        return false;
    }
}
