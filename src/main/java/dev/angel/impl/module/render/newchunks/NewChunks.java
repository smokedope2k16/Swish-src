package dev.angel.impl.module.render.newchunks;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.impl.module.render.newchunks.util.ChunkData;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

public class NewChunks extends Module {
    protected final List<ChunkData> chunkDataList = new ArrayList<>();
    protected final List<WorldChunk> loadedChunks = new ArrayList<>();

    public NewChunks() {
        super("NewChunks", new String[]{"newchunks", "newchunk"}, "Renders chunks generated in 1.19", Category.RENDER);
        this.offerListeners(new ListenerUpdate(this), new ListenerRender(this));
    }

    @Override
    public void onEnable() {
        this.chunkDataList.clear();
        this.loadedChunks.clear();
    }

    @Override
    public void onDisable() {
        this.chunkDataList.clear();
        this.loadedChunks.clear();
    }
}
