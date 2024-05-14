package dev.angel.api.util.math;

import dev.angel.api.interfaces.Minecraftable;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;

public class RotationsUtil implements Minecraftable {

    public static float[] getRotations(BlockPos pos, Direction facing) {
        return getRotations(pos, facing, mc.player);
    }

    public static float[] getRotations(BlockPos pos, Direction facing, Entity from) {
        return getRotations(pos, facing, from, mc.world, mc.world.getBlockState(pos));
    }

    public static float[] getRotations(BlockPos pos, Direction facing, Entity from, ClientWorld world, BlockState state) {
        Box bb = state.getSidesShape(world, pos).getBoundingBox();

        double x = pos.getX() + (bb.minX + bb.maxX) / 2.0;
        double y = pos.getY() + (bb.minY + bb.maxY) / 2.0;
        double z = pos.getZ() + (bb.minZ + bb.maxZ) / 2.0;

        if (facing != null) {
            x += facing.getVector().getX() * ((bb.minX + bb.maxX) / 2.0);
            y += facing.getVector().getY() * ((bb.minY + bb.maxY) / 2.0);
            z += facing.getVector().getZ() * ((bb.minZ + bb.maxZ) / 2.0);
        }

        return getRotations(x, y, z, from);
    }

    public static float[] getRotations(double x, double y, double z, Entity f) {
        return getRotations(x, y, z, f.getX(), f.getY(), f.getZ(), f.getEyeHeight(f.getPose()));
    }

    public static float[] getRotations(Entity from, Entity entity, double height, double maxAngle) {
        return getRotations(entity, from.getX(), (float) from.getY(), from.getZ(), from.getEyeHeight(entity.getPose()), height, maxAngle);
    }

    public static float[] getRotations(Entity entity, double fromX, float fromY, double fromZ, float eyeHeight, double height, double maxAngle) {
        float[] rotations = RotationsUtil.getRotations(
                entity.getX(), entity.getY() + entity.getEyeHeight(entity.getPose()) * height,
                entity.getZ(), fromX, fromY, fromZ, eyeHeight);
        return smoothen(rotations, maxAngle);
    }

    public static float[] getRotations(double x, double y, double z, double fromX, double fromY, double fromZ, float fromHeight) {
        double xDiff = x - fromX;
        double yDiff = y - (fromY + fromHeight);
        double zDiff = z - fromZ;
        double dist = MathHelper.sqrt((float) (xDiff * xDiff + zDiff * zDiff));

        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) (-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        float prevYaw = 0;
        if (mc.player != null) {
            prevYaw = mc.player.prevYaw;
            float diff = yaw - prevYaw;

            if (diff < -180.0f || diff > 180.0f) {
                float round = Math.round(Math.abs(diff / 360.0f));
                diff = diff < 0.0f ? diff + 360.0f * round : diff - (360.0f * round);
            }

            return new float[]{prevYaw + diff, pitch};
        }
        return new float[0];
    }

    public static float[] smoothen(float[] rotations, double maxAngle) {
        if (mc.player != null) {
            float[] server = {mc.player.getYaw(), mc.player.getPitch()};
            return smoothen(server, rotations, maxAngle);
        }
        return rotations;
    }

    public static float[] smoothen(float[] server, float[] rotations, double maxAngle) {
        if (maxAngle >= 180.0f || maxAngle <= 0.0f || angle(server, rotations) <= maxAngle) {
            return rotations;
        }
        return faceSmoothly(server[0], server[1], rotations[0], rotations[1], maxAngle, maxAngle);
    }

    public static float[] faceSmoothly(double curYaw, double curPitch, double intendedYaw, double intendedPitch, double yawSpeed, double pitchSpeed) {
        float yaw = updateRotation((float) curYaw, (float) intendedYaw, (float) yawSpeed);

        float pitch = updateRotation((float) curPitch, (float) intendedPitch, (float) pitchSpeed);

        return new float[]{yaw, pitch};
    }

    public static float updateRotation(float current, float intended, float factor) {
        float updated = MathHelper.wrapDegrees(intended - current);

        if (updated > factor) {
            updated = factor;
        }

        if (updated < -factor) {
            updated = -factor;
        }

        return current + updated;
    }

    public static double angle(float[] rotation1, float[] rotation2) {
        Vec3d r1Vec = getVec3d(rotation1[0], rotation1[1]);
        Vec3d r2Vec = getVec3d(rotation2[0], rotation2[1]);
        return MathUtil.angle(r1Vec, r2Vec);
    }


    public static Vec3d getVec3d(float yaw, float pitch) {
        float vx = -MathHelper.sin(MathUtil.rad(yaw)) * MathHelper.cos(MathUtil.rad(pitch));
        float vz = MathHelper.cos(MathUtil.rad(yaw)) * MathHelper.cos(MathUtil.rad(pitch));
        float vy = -MathHelper.sin(MathUtil.rad(pitch));
        return new Vec3d(vx, vy, vz);
    }

    public static double normalizeAngle(Double angleIn) {
        double angle = angleIn;
        if ((angle %= 360.0) >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    public static Vec2f getRotationTo(Vec3d posTo, Vec3d posFrom) {
        return getRotationFromVec(posTo.subtract(posFrom));
    }

    public static Vec2f getRotationFromVec(Vec3d vec) {
        double xz = Math.hypot(vec.x, vec.z);
        float yaw = (float) normalizeAngle(Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0);
        float pitch = (float) normalizeAngle(Math.toDegrees(-Math.atan2(vec.y, xz)));
        return new Vec2f(yaw, pitch);
    }

}