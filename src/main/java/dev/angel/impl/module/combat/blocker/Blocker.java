package dev.angel.impl.module.combat.blocker;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.Value;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Blocker extends Module {

    protected final Value<Boolean> swing = new Value<>(
            new String[]{"Swing", "punch"},
            "Swing hand when placing",
            true
    );

    public Blocker() {
        super("Blocker", new String[]{"blocker", "surroundblocker", "anticity"}, "Blocks people from mining your surround", Category.COMBAT);
    }

    protected boolean isSurroundBlock(BlockPos pos) {
        assert mc.player != null;
        Vec3d playerVec = mc.player.getPos();
        int x = (int) playerVec.x;
        int y = (int) playerVec.y;
        int z = (int) playerVec.z;
        BlockPos playerPos = new BlockPos(x, y, z);

        for (BlockPos off : surroundOffsets) {
            if (pos.equals(playerPos.add(off))) {
                return true;
            }
        }

        return false;
    }

    protected final BlockPos[] surroundOffsets = new BlockPos[] {
            new BlockPos(1, 0, 0),
            new BlockPos(-1, 0, 0),
            new BlockPos(0, 0, 1),
            new BlockPos(0, 0, -1)
    };

    protected final BlockPos[] blockOffsets = new BlockPos[] {
            new BlockPos(0, 1, 0)
    };

}
