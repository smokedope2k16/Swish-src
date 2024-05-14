package dev.angel.api.util.math;

import dev.angel.api.interfaces.Minecraftable;

public class AngleUtil implements Minecraftable {

// sohut out to exeter
    public static boolean isAiming(float yaw, float pitch, int fov) {
        if (mc.player != null) {
            yaw = wrapAngleTo180(yaw);
            pitch = wrapAngleTo180(pitch);
            float curYaw = wrapAngleTo180(mc.player.getYaw());
            float curPitch = wrapAngleTo180(mc.player.getPitch());
            float yawDiff = Math.abs(yaw - curYaw);
            return yawDiff + Math.abs(pitch - curPitch) <= (float) fov;
        }
        return false;
    }

    public static float wrapAngleTo180(float angle) {
        if ((angle %= 360.0f) >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }
}
