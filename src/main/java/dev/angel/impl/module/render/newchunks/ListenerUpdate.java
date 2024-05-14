package dev.angel.impl.module.render.newchunks;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.impl.events.UpdateEvent;
import dev.angel.impl.module.render.newchunks.util.ChunkData;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class ListenerUpdate extends ModuleListener<NewChunks, UpdateEvent> {
    public ListenerUpdate(NewChunks module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        final ChunkPos chunkPos = mc.player.getChunkPos();
        final List<WorldChunk> chonkers = loopChunks(chunkPos.x, chunkPos.z);

        for (WorldChunk chunk : chonkers) {
            if (chunk == null) {
                continue;
            }

            final List<Block> blocks = new ArrayList<>();

            Arrays.stream(chunk.getSectionArray()).forEach(section -> section.getBlockStateContainer().forEachValue(blockState -> blocks.add(blockState.getBlock())));

            if (!blocks.contains(Blocks.COPPER_ORE) && mc.world.getRegistryKey().getValue().getPath().equals("overworld")
                    || !blocks.contains(Blocks.NETHER_GOLD_ORE) && mc.world.getRegistryKey().getValue().getPath().equals("the_nether")) {
                continue;
            }

            final ChunkData chunkData = new ChunkData(chunk.getPos().x, chunk.getPos().z);
            if (module.chunkDataList.contains(chunkData)) {
                continue;
            }

            module.chunkDataList.add(chunkData);
        }
    }

    private List<WorldChunk> loopChunks(int posX, int posZ) {
        final List<WorldChunk> chunks = new ArrayList<>();

        for (int x = -17; x <= 17; x++) {
            for (int z = -17; z <= 17; z++) {
                final WorldChunk chunk = mc.world.getChunkManager().getWorldChunk(posX + x, posZ + z);
                if (chunk == null) {
                    continue;
                }

                if (module.loadedChunks.contains(chunk)) {
                    continue;
                }

                module.loadedChunks.add(chunk);
                chunks.add(chunk);
            }
        }

        return chunks;
    }
}
