package dev.angel.api.util.block;

import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.util.render.Interpolation;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class HoleUtil implements Minecraftable {
    public static BlockPos[] holeOffsets = new BlockPos[] {
            new BlockPos(1, 0, 0),
            new BlockPos(-1, 0, 0),
            new BlockPos(0, 0, 1),
            new BlockPos(0, 0, -1),
            new BlockPos(0, -1, 0)
    };

    public static boolean isInHole(PlayerEntity player) {
        int x = (int) player.getX();
        int y = (int) player.getY();
        int z = (int) player.getZ();
        BlockPos playerPos = new BlockPos(x, y, z);
        return isHole(playerPos);
    }

    public static boolean isInHole(PlayerEntity player, Hole hole) {
        int x = (int) player.getX();
        int y = (int) player.getY();
        int z = (int) player.getZ();
        BlockPos playerPos = new BlockPos(x, y, z);
        return playerPos.equals(hole.pos1);
    }

    public static boolean hasEntities(Hole hole) {
        assert mc.world != null;
        Box box = new Box(hole.pos1);
        if (hole.pos2 != null) {
            box = new Box(
                    hole.pos1.getX() - Interpolation.getCameraPos().x,
                    hole.pos1.getY() - Interpolation.getCameraPos().y,
                    hole.pos1.getZ() - Interpolation.getCameraPos().z,
                    hole.pos2.getX() + 1 - Interpolation.getCameraPos().x,
                    hole.pos2.getY() + 1 - Interpolation.getCameraPos().y,
                    hole.pos2.getZ() + 1 - Interpolation.getCameraPos().z
            );
        }
        for (Entity entity : mc.world.getOtherEntities(null, box)) {
            if (!(entity instanceof ItemEntity) && !(entity instanceof ExperienceOrbEntity) && !(entity instanceof ArrowEntity)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHole(BlockPos pos) {
        return isObbyHole(pos) || isBedrockHole(pos);
    }

    public static boolean isObbyHole(BlockPos pos) {
        assert mc.world != null;
        if (isBedrockHole(pos)) return false;
        if (!mc.world.isAir(pos.up()) || !mc.world.isAir(pos.up().up())) return false;
        for (BlockPos off : holeOffsets) {
            if (!isSafeBlock(pos.add(off))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBedrockHole(BlockPos pos) {
        assert mc.world != null;
        if (!mc.world.isAir(pos.up()) || !mc.world.isAir(pos.up().up())) return false;
        for (BlockPos off : holeOffsets) {
            if (mc.world.getBlockState(pos.add(off)).getBlock() != Blocks.BEDROCK) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDoubleHole(BlockPos pos) {
        assert mc.world != null;
        Hole hole = getDoubleHole(pos);
        if (hole == null) {
            return false;
        }
        if (!mc.world.isAir(hole.pos1.up()) || !mc.world.isAir(hole.pos2.up())) {
            return false;
        }
        return true;
    }

    public static Hole getDoubleHole(BlockPos pos) {
        if (checkOffset(pos, 1, 0)) {
            return new Hole(pos, pos.add(1, 0, 0), false);
        }
        else if (checkOffset(pos, 0, 1)) {
            return new Hole(pos, pos.add(0, 0, 1), false);
        }
        return null;
    }

    public static Vec3d getCenter(Hole hole) {
        assert mc.player != null;
        double x = hole.pos1.getX() + 0.5;
        double z = hole.pos1.getZ() + 0.5;
        if (hole.pos2 != null) {
            x = ((hole.pos1.getX() + 0.5) + (hole.pos2.getX() + 0.5)) / 2.0;
            z = ((hole.pos1.getZ() + 0.5) + (hole.pos2.getZ() + 0.5)) / 2.0;
        }

        return new Vec3d(x, mc.player.getY(), z);
    }

    public static boolean isSafeBlock(BlockPos pos) {
        assert mc.world != null;
        return mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN
                || mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK
                || mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST;
    }

    public static boolean checkOffset(BlockPos pos, int offX, int offZ) {
        assert mc.world != null;
        return mc.world.isAir(pos)
                && mc.world.isAir(pos.add(offX, 0, offZ))
                && isSafeBlock(pos.add(0, -1, 0))
                && isSafeBlock(pos.add(offX, -1, offZ))
                && isSafeBlock(pos.add(offX * 2, 0, offZ * 2))
                && isSafeBlock(pos.add(-offX, 0, -offZ))
                && isSafeBlock(pos.add(offZ, 0, offX))
                && isSafeBlock(pos.add(-offZ, 0, -offX))
                && isSafeBlock(pos.add(offX, 0, offZ).add(offZ, 0, offX))
                && isSafeBlock(pos.add(offX, 0, offZ).add(-offZ, 0, -offX));
    }

    public static List<Hole> getHoles(float range, boolean doubles) {
        List<Hole> holes = new ArrayList<>();

        for (BlockPos pos : BlockUtil.getSphere(mc.player, range, false)) {
            if (!mc.world.isAir(pos)) {
                continue;
            }
            if (isBedrockHole(pos)) {
                holes.add(new Hole(pos, true));
                continue;
            }
            else if (isObbyHole(pos)) {
                holes.add(new Hole(pos, false));
                continue;
            }
            if (doubles && isDoubleHole(pos)) {
                holes.add(getDoubleHole(pos));
            }
        }

        return holes;
    }

    public static class Hole {
        public BlockPos pos1;
        public BlockPos pos2;
        public boolean safe;

        public Hole(BlockPos pos1, boolean safe) {
            this.pos1 = pos1;
            this.safe = safe;
        }

        public Hole(BlockPos pos1, BlockPos pos2, boolean safe) {
            this.pos1 = pos1;
            this.pos2 = pos2;
            this.safe = safe;
        }
    }
}
