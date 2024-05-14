package dev.angel.impl.events;

import dev.angel.api.event.events.Event;
import net.minecraft.block.BlockState;

public class RenderBlockSideEvent extends Event {
    private final BlockState blockState;

    public RenderBlockSideEvent(BlockState blockState) {
        this.blockState = blockState;
    }

    public BlockState getBlockState() {
        return blockState;
    }
}
