package dev.angel.impl.module.player.optimizer;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.inventory.ItemUtil;
import dev.angel.impl.events.PacketEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class ListenerAttack extends ModuleListener<Optimizer, PacketEvent.Send<PlayerInteractEntityC2SPacket>> {
    public ListenerAttack(Optimizer module) {
        super(module, PacketEvent.Send.class, PlayerInteractEntityC2SPacket.class);
    }

    @Override
    public void call(PacketEvent.Send<PlayerInteractEntityC2SPacket> event) {
        event.getPacket().handle(new PlayerInteractEntityC2SPacket.Handler() {
            @Override
            public void interact(Hand hand) {
                // spent a million on a rollie lets get it
            }

            @Override
            public void interactAt(Hand hand, Vec3d pos) {
                // im knocking on the jeweler door hold up lets get it
            }

            @Override
            public void attack() {
                HitResult hitResult = mc.crosshairTarget;
                if (hitResult != null && mc.player != null) {
                    if (hitResult.getType() == HitResult.Type.ENTITY) {
                        EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                        Entity entity = entityHitResult.getEntity();
                        if (entity instanceof EndCrystalEntity) {
                            StatusEffectInstance weakness = mc.player.getStatusEffect(StatusEffects.WEAKNESS);
                            StatusEffectInstance strength = mc.player.getStatusEffect(StatusEffects.STRENGTH);
                            boolean badEffects = weakness != null && (strength == null || strength.getAmplifier() <= weakness.getAmplifier());
                            if (badEffects && ItemUtil.isTool(mc.player.getMainHandStack())) {
                                return;
                            }

                            entity.kill();
                            entity.setRemoved(Entity.RemovalReason.KILLED);
                            entity.onRemoved();
                        }
                    }
                }
            }
        });
    }
}
