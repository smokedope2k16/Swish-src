package dev.angel.api.util.math;

import dev.angel.api.interfaces.Minecraftable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Optional;

public class RayTraceUtil implements Minecraftable {

   // public static RaycastContext getRayTraceResult(float yaw, float pitch) {
 //       return getRayTraceResult(yaw, pitch, mc.interactionManager.getReachDistance());
    //}

   /* public static RaycastContext getRayTraceResult(float yaw, float pitch, float distance) {
        Vec3d vec3d = PositionUtil.getEyesPos(mc.player);
        Vec3d lookVec = RotationUtil.getVec3d(yaw, pitch);
        Vec3d rotations = vec3d.add(lookVec.x * (double)distance, lookVec.y * (double)distance, lookVec.z * (double)distance);
        return Optional.ofNullable(
                mc.world.raycastBlock(vec3d, rotations, BlockPos.ofFloored(vec3d), null, null).orElseGet(()
                -> new RayTraceResult(RayTraceResult.Type.MISS, new Vec3d(0.5, 1.0, 0.5), EnumFacing.UP, BlockPos.ORIGIN));
    }
 */
}
