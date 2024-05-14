package dev.angel.impl.manager;

import dev.angel.Swish;
import dev.angel.api.event.bus.Listener;
import dev.angel.api.event.bus.SubscriberImpl;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.impl.events.DeathEvent;
import dev.angel.impl.events.EntityWorldEvent;
import dev.angel.impl.events.PacketEvent;
import dev.angel.impl.events.PopEvent;
import dev.angel.impl.module.misc.popcounter.PopCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.HashMap;
import java.util.Map;

public class PopManager extends SubscriberImpl implements Minecraftable {

    private final Map<String, Integer> popMap = new HashMap<>();

    public PopManager() {
        this.listeners.add(new Listener<PacketEvent.Receive<EntityStatusS2CPacket>>
                (PacketEvent.Receive.class, Integer.MIN_VALUE, EntityStatusS2CPacket.class) {
            @Override
            public void call(PacketEvent.Receive<EntityStatusS2CPacket> event) {
                if (mc.player == null || mc.world == null) {
                    return;
                }

                final EntityStatusS2CPacket packet = event.getPacket();

                if (packet.getStatus() == 35) {
                    final Entity entity = packet.getEntity(mc.world);
                    if (entity != null) {
                        String name = EntityUtil.getName(entity);
                        if (entity instanceof PlayerEntity) {
                            boolean contains = popMap.containsKey(name);
                            popMap.put(name, contains ? popMap.get(name) + 1 : 1);

                            PopEvent popEvent = new PopEvent((PlayerEntity) entity);
                            Swish.getEventBus().dispatch(popEvent);
                        }
                    }
                }
            }
        });
        this.listeners.add(new Listener<>(DeathEvent.class, Integer.MIN_VALUE) {
            @Override
            public void call(DeathEvent event) {
                final Entity entity = event.getEntity();
                if (entity instanceof PlayerEntity) {
                    String name = EntityUtil.getName(entity);
                    if (popMap.containsKey(name)) {
                        popMap.remove(name, popMap.get(name));
                    }
                }
            }
        });
        this.listeners.add(new Listener<EntityWorldEvent.Remove>(EntityWorldEvent.class, Integer.MIN_VALUE) {
            @Override
            public void call(EntityWorldEvent.Remove event) {
                if (Swish.getModuleManager().get(PopCounter.class).visualRange()) {
                    final Entity entity = event.getEntity();
                    if (entity instanceof PlayerEntity) {
                        String name = EntityUtil.getName(entity);
                        if (popMap.containsKey(name)) {
                            popMap.remove(name, popMap.get(name));
                        }
                    }
                }
            }
        });
    }

    public final Map<String, Integer> getPopMap() {
        return popMap;
    }
}
