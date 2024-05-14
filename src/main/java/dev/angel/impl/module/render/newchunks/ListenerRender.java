package dev.angel.impl.module.render.newchunks;

import dev.angel.api.event.listener.ModuleListener;
import dev.angel.api.util.render.Interpolation;
import dev.angel.api.util.render.RenderMethods;
import dev.angel.impl.events.Render3DEvent;
import dev.angel.impl.module.other.colours.Colours;
import dev.angel.impl.module.render.newchunks.util.ChunkData;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

@SuppressWarnings("ConstantConditions")
public class ListenerRender extends ModuleListener<NewChunks, Render3DEvent> {
    public ListenerRender(NewChunks module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (module.chunkDataList.isEmpty()) {
            return;
        }

        final MatrixStack matrix = event.getMatrix();

        RenderMethods.enable3D();
        RenderMethods.color(Colours.get().color());

        matrix.push();

        for (ChunkData chunkData : module.chunkDataList) {
            if (chunkData != null) {
                final int posX = chunkData.getX() * 16;
                final int posY = mc.world.getRegistryKey().getValue().getPath().equals("overworld") ? -64 : 0;
                final int posZ = chunkData.getZ() * 16;
                final Box bb = new Box(posX, posY, posZ, posX + 16.0, posY, posZ + 16.0);
                if (Interpolation.getCameraPos().distanceTo(bb.getCenter()) > 250.0D) {
                    continue;
                }

                RenderMethods.drawPlane(matrix, bb.offset(-Interpolation.getCameraPos().x, -Interpolation.getCameraPos().y, -Interpolation.getCameraPos().z));
            }
        }

        matrix.pop();

        RenderMethods.resetColor();
        RenderMethods.disable3D();
    }
}
