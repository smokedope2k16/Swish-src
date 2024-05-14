package dev.angel.impl.events;

import dev.angel.api.event.events.Event;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DamageBlockEvent extends Event {
    private final BlockPos pos;
    private final Direction direction;

    public DamageBlockEvent(BlockPos pos, Direction direction) {
        this.pos = pos;
        this.direction = direction;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Direction getDirection() {
        return direction;
    }
}
