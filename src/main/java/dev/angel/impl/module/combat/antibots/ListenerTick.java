package dev.angel.impl.module.combat.antibots;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.api.util.network.LatencyUtil;
import dev.angel.impl.command.FakePlayerCommand;
import dev.angel.impl.events.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class ListenerTick extends ModuleListener<AntiBots, TickEvent.Post> {
    public ListenerTick(AntiBots module) {
        super(module, TickEvent.Post.class);
    }

    @Override
    public void call(TickEvent.Post event) {
        if (mc.world != null && mc.player != null) {
            for (Entity entity : mc.world.getEntities()) {// -2147483647 fakeplayr
                if (entity instanceof PlayerEntity && entity != FakePlayerCommand.fakePlayer && entity != mc.player) {
                    try {
                        if (module.gamemode.getValue() && EntityUtil.getGameMode((PlayerEntity) entity) == null) {
                            entity.remove(Entity.RemovalReason.DISCARDED);
                        }
                        if (module.uuid.getValue() && EntityUtil.getUUID((PlayerEntity) entity) == null) {
                            entity.remove(Entity.RemovalReason.DISCARDED);
                        }
                        if (module.name.getValue() && EntityUtil.getProfile((PlayerEntity) entity) == null) {
                            entity.remove(Entity.RemovalReason.DISCARDED);
                        }
                        if (module.ping.getValue() && (LatencyUtil.getPing(entity) > 1)) {
                            entity.remove(Entity.RemovalReason.DISCARDED);
                        }
                    } catch (Exception e) {
                        // this is needed lol
                    }
                }
            }
        }
    }
}
