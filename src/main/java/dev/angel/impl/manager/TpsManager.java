package dev.angel.impl.manager;

import dev.angel.api.event.bus.Listener;
import dev.angel.api.event.bus.SubscriberImpl;
import dev.angel.api.interfaces.Minecraftable;
import dev.angel.impl.events.PacketEvent;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import java.util.ArrayDeque;

public class TpsManager extends SubscriberImpl implements Minecraftable {

    private final ArrayDeque<Float> queue = new ArrayDeque<>(20);
    private float currentTps;
    private long time;
    private float tps;

    public TpsManager() {
        this.listeners.add(new Listener<PacketEvent.Receive<WorldTimeUpdateS2CPacket>>(PacketEvent.Receive.class, WorldTimeUpdateS2CPacket.class) {
            @Override
            public void call(PacketEvent.Receive<WorldTimeUpdateS2CPacket> event) {
                if (time != 0) {
                    if (queue.size() > 20) {
                        queue.poll();
                    }

                    currentTps = Math.max(0.0f, Math.min(20.0f, 20.0f * (1000.0f / (System.currentTimeMillis() - time))));
                    queue.add(currentTps);
                    float factor = 0.0f;
                    for (Float qTime : queue) {
                        factor += Math.max(0.0f, Math.min(20.0f, qTime));
                    }

                    if (queue.size() > 0) {
                        factor /= queue.size();
                    }
                    tps = factor;
                }

                time = System.currentTimeMillis();
            }
        });
    }

    public float getTps() {
        return tps;
    }
}
