package dev.angel.api.util.entity;

import com.mojang.authlib.GameProfile;
import dev.angel.Swish;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.asm.ducks.IExplosion;
import dev.angel.asm.ducks.IRaycastContext;
import dev.angel.asm.ducks.IVec3d;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameMode;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.explosion.Explosion;
import org.joml.Vector3d;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class EntityUtil implements Minecraftable {

    private static final Vec3d vec3d = new Vec3d(0, 0, 0);
    public static Explosion explosion;
    public static RaycastContext raycastContext;

    public static PlayerEntity getClosestEnemy() {
        PlayerEntity closest = null;
        double distance = Float.MAX_VALUE;
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player != null && player.isAlive() && !player.equals(mc.player) && !Swish.getFriendManager().isFriend(player)) {
                Vec3d pos = mc.player.getPos();
                double dist = player.squaredDistanceTo(pos.x, pos.y, pos.z);
                if (dist < distance) {
                    closest = player;
                    distance = dist;
                }
            }
        }

        return closest;
    }

    public static float[] getRotations(Entity entity) {
        double positionX = entity.getX() - mc.player.getX();
        double positionZ = entity.getZ() - mc.player.getZ();
        double positionY = entity.getZ() + (double) entity.getEyeHeight(entity.getPose()) / 1.3 - mc.player.getY() + (double) mc.player.getEyeHeight(mc.player.getPose());
        double positions = MathHelper.sqrt((float) (positionX * positionX + positionZ * positionZ));
        float yaw = (float) (Math.atan2(positionZ, positionX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) (-(Math.atan2(positionY, positions) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static Vector3d set(Vector3d vec, Entity entity, double tickDelta) {
        vec.x = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
        vec.y = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
        vec.z = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());

        return vec;
    }

    public static GameMode getGameMode(PlayerEntity player) {
        if (player != null) {
            PlayerListEntry playerListEntry = Objects.requireNonNull(mc.getNetworkHandler()).getPlayerListEntry(player.getUuid());
            if (playerListEntry != null) {
                return playerListEntry.getGameMode();
            }
        }

        return null;
    }

    public static UUID getUUID(PlayerEntity player) {
        if (player != null) {
            return player.getUuid();
        }

        return null;
    }

    public static GameProfile getProfile(PlayerEntity player) {
        if (player != null) {
            PlayerListEntry playerListEntry = Objects.requireNonNull(mc.getNetworkHandler()).getPlayerListEntry(player.getUuid());
            if (playerListEntry != null) {
                return playerListEntry.getProfile();
            }
        }
        return null;
    }

    public static float getHealth(PlayerEntity player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    public static String getName(Entity entity) {
        return entity.getName().getString();
    }

    public static double crystalDamage(PlayerEntity player, Vec3d crystal, boolean predictMovement, BlockPos obsidianPos, boolean ignoreTerrain) {
        if (player == null) return 0;
        ((IVec3d) vec3d).set(player.getPos().x, player.getPos().y, player.getPos().z);
        if (predictMovement)
            ((IVec3d) vec3d).set(vec3d.x + player.getVelocity().x, vec3d.y + player.getVelocity().y, vec3d.z + player.getVelocity().z);

        double modDistance = Math.sqrt(vec3d.squaredDistanceTo(crystal));
        if (modDistance > 12) return 0;

        double exposure = getExposure(crystal, player, predictMovement, raycastContext, obsidianPos, ignoreTerrain);
        double impact = (1 - (modDistance / 12)) * exposure;
        double damage = ((impact * impact + impact) / 2 * 7 * (6 * 2) + 1);

        damage = getDamageForDifficulty(damage);
        damage = DamageUtil.getDamageLeft((float) damage, (float) player.getArmor(), (float) Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)).getValue());
        damage = resistanceReduction(player, damage);

        ((IExplosion) explosion).set(crystal, 6, false);
        damage = blastProtReduction(player, damage, explosion);

        return damage < 0 ? 0 : damage;
    }

    private static double getDamageForDifficulty(double damage) {
        return switch (mc.world.getDifficulty()) {
            case PEACEFUL -> 0;
            case EASY -> Math.min(damage / 2 + 1, damage);
            case HARD -> damage * 3 / 2;
            default -> damage;
        };
    }

    private static double blastProtReduction(Entity player, double damage, Explosion explosion) {
        int protLevel = EnchantmentHelper.getProtectionAmount(player.getArmorItems(), Objects.requireNonNull(mc.world).getDamageSources().explosion(explosion));
        if (protLevel > 20) protLevel = 20;

        damage *= (1 - (protLevel / 25.0));
        return damage < 0 ? 0 : damage;
    }

    private static double resistanceReduction(LivingEntity player, double damage) {
        if (player.hasStatusEffect(StatusEffects.RESISTANCE)) {
            int lvl = (Objects.requireNonNull(player.getStatusEffect(StatusEffects.RESISTANCE)).getAmplifier() + 1);
            damage *= (1 - (lvl * 0.2));
        }

        return damage < 0 ? 0 : damage;
    }

    private static double getExposure(Vec3d source, Entity entity, boolean predictMovement, RaycastContext raycastContext, BlockPos obsidianPos, boolean ignoreTerrain) {
        Box box = entity.getBoundingBox();
        if (predictMovement) {
            Vec3d v = entity.getVelocity();
            box = box.offset(v.x, v.y, v.z);
        }

        double d = 1 / ((box.maxX - box.minX) * 2 + 1);
        double e = 1 / ((box.maxY - box.minY) * 2 + 1);
        double f = 1 / ((box.maxZ - box.minZ) * 2 + 1);
        double g = (1 - Math.floor(1 / d) * d) / 2;
        double h = (1 - Math.floor(1 / f) * f) / 2;

        if (!(d < 0) && !(e < 0) && !(f < 0)) {
            int i = 0;
            int j = 0;

            for (double k = 0; k <= 1; k += d) {
                for (double l = 0; l <= 1; l += e) {
                    for (double m = 0; m <= 1; m += f) {
                        double n = MathHelper.lerp(k, box.minX, box.maxX);
                        double o = MathHelper.lerp(l, box.minY, box.maxY);
                        double p = MathHelper.lerp(m, box.minZ, box.maxZ);

                        ((IVec3d) vec3d).set(n + g, o, p + h);
                        ((IRaycastContext) raycastContext).set(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity);

                        if (raycast(raycastContext, obsidianPos, ignoreTerrain).getType() == HitResult.Type.MISS) i++;

                        j++;
                    }
                }
            }

            return (double) i / j;
        }

        return 0;
    }

    private static BlockHitResult raycast(RaycastContext context, BlockPos obsidianPos, boolean ignoreTerrain) {
        return BlockView.raycast(context.getStart(), context.getEnd(), context, (raycastContext, blockPos) -> {
            BlockState blockState;
            if (blockPos.equals(obsidianPos)) blockState = Blocks.OBSIDIAN.getDefaultState();
            else {
                blockState = Objects.requireNonNull(mc.world).getBlockState(blockPos);
                if (blockState.getBlock().getBlastResistance() < 600 && ignoreTerrain)
                    blockState = Blocks.AIR.getDefaultState();
            }

            Vec3d vec3d = raycastContext.getStart();
            Vec3d vec3d2 = raycastContext.getEnd();

            VoxelShape voxelShape = raycastContext.getBlockShape(blockState, mc.world, blockPos);
            BlockHitResult blockHitResult = Objects.requireNonNull(mc.world).raycastBlock(vec3d, vec3d2, blockPos, voxelShape, blockState);
            VoxelShape voxelShape2 = VoxelShapes.empty();
            BlockHitResult blockHitResult2 = voxelShape2.raycast(vec3d, vec3d2, blockPos);

            double d = blockHitResult == null ? Double.MAX_VALUE : raycastContext.getStart().squaredDistanceTo(blockHitResult.getPos());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : raycastContext.getStart().squaredDistanceTo(blockHitResult2.getPos());

            return d <= e ? blockHitResult : blockHitResult2;
        }, (raycastContext) -> {
            Vec3d vec3d = raycastContext.getStart().subtract(raycastContext.getEnd());
            return BlockHitResult.createMissed(raycastContext.getEnd(), Direction.getFacing(vec3d.x, vec3d.y, vec3d.z), BlockPos.ofFloored(raycastContext.getEnd()));
        });
    }

    public static double getDefaultMoveSpeed() {
        double baseSpeed = 0.2873;
        StatusEffectInstance speed = mc.player.getStatusEffect(StatusEffects.SPEED);
        if (speed != null) {
            int amplifier = speed.getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
        }

        return baseSpeed;
    }

    public static boolean isMoving() {
        return mc.player.forwardSpeed != 0.0 || mc.player.sidewaysSpeed != 0.0;
    }

}