package dev.angel.api.util.render;

import dev.angel.api.interfaces.Minecraftable;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class Interpolation implements Minecraftable {

    public static Vec3d interpolateEntity(Entity entity) {
        double x;
        double y;
        double z;

        {
            x = interpolateLastTickPos(entity.getX(), entity.prevX) - getCameraPos().x;
            y = interpolateLastTickPos(entity.getY(), entity.prevY) - getCameraPos().y;
            z = interpolateLastTickPos(entity.getZ(), entity.prevZ) - getCameraPos().z;
        }

        return new Vec3d(x, y, z);
    }

    public static double interpolateLastTickPos(double pos, double lastPos) {
        return lastPos + (pos - lastPos) * mc.getTickDelta();
    }

    public static Box interpolatePos(BlockPos pos) {
        return interpolatePos(pos, 1.0F);
    }

    public static Box interpolatePos(BlockPos pos, float height) {
        return new Box(
                pos.getX() - getCameraPos().x,
                pos.getY() - getCameraPos().y,
                pos.getZ() - getCameraPos().z,
                pos.getX() - getCameraPos().x + 1,
                pos.getY() - getCameraPos().y + height,
                pos.getZ() - getCameraPos().z + 1
        );
    }

    public static Box interpolateAxis(Box bb) {
        return new Box(
                bb.minX - getCameraPos().x,
                bb.minY - getCameraPos().y,
                bb.minZ - getCameraPos().z,
                bb.maxX - getCameraPos().x,
                bb.maxY - getCameraPos().y,
                bb.maxZ - getCameraPos().z
        );
    }

    public static Vec3d getCameraPos() {
        Camera camera = mc.getBlockEntityRenderDispatcher().camera;
        if (camera == null) {
            return Vec3d.ZERO;
        }

        return camera.getPos();
    }
}
