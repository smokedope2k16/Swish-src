package dev.angel.impl.module.render.killeffect;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.DeathEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.concurrent.ThreadLocalRandom;

public class ListenerDeath extends ModuleListener<KillEffect, DeathEvent> {
    public ListenerDeath(KillEffect module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        if (mc.world == null || mc.player == null) {
            return;
        }

        if (event.getEntity() instanceof PlayerEntity player && player != mc.player) {
            final LightningEntity bolt = new LightningEntity(EntityType.LIGHTNING_BOLT, mc.world);
            bolt.updatePositionAndAngles(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
            mc.world.addEntity(ThreadLocalRandom.current().nextInt(), bolt);
        }
    }
}
