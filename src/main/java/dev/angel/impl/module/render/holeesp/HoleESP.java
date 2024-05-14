package dev.angel.impl.module.render.holeesp;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.util.math.PositionUtil;
import dev.angel.api.value.ColorValue;
import dev.angel.api.value.EnumValue;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import dev.angel.impl.module.render.holeesp.mode.OutlineMode;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.angel.api.util.block.HoleUtil.Hole;

public class HoleESP extends Module {

    protected final NumberValue<Integer> range = new NumberValue<>(
            new String[]{"Range", "rng", "rang"},
            "How far to calcualte holes from",
            8, 0, 15
    );

    protected final Value<Boolean> doubles = new Value<>(
            new String[]{"2x1", "2x1holes", "doubles"},
            "Draws holes that are 2 blocks long and 1 block wide",
            true
    );

    protected final Value<Boolean> voids = new Value<>(
            new String[]{"VoidHoles", "void", "voids"},
            "Draws void holes",
            true
    );

    protected final NumberValue<Float> height = new NumberValue<>(
            new String[]{"Height"},
            "Height of holes",
            1.0f, -1.0f, 1.0f, 0.1f
    );

    protected final EnumValue<OutlineMode> outlineMode = new EnumValue<>(
            new String[]{"Outline", "outlinemode"},
            "Which way to outline the hole",
            OutlineMode.NORMAL
    );

    protected final NumberValue<Integer> wireAlpha = new NumberValue<>(
            new String[]{"WireAlpha", "alpha"},
            "Alpha of the outlines",
            125, 0, 255
    );

    protected final ColorValue obbyColor = new ColorValue(
                    new String[]{"ObsidianColor", "obsidiancolour", "ObbyColor", "ObbyColour"},
                    new Color(0x38FF0000, true),
                    false
            );

    protected final ColorValue bedrockColor = new ColorValue(
                    new String[]{"BedrockColor", "BedrockColour"},
                    new Color(0x3800FF00, true),
                    false

            );

    protected final ColorValue voidColor = new ColorValue(
                    new String[]{"VoidColor", "VoidColour"},
                    new Color(0x380000FF, true),
                    false
            );


    ExecutorService service = Executors.newCachedThreadPool();
    protected List<Hole> holes = new ArrayList<>();
    protected List<BlockPos> voidHoles = new ArrayList<>();

    public HoleESP() {
        super("HoleESP", new String[]{"HoleEsp", "holeingtons", "VoidEsp"}, "Draws void holes & combat holes", Category.RENDER);
        this.offerValues(range, doubles, voids, height, outlineMode, wireAlpha, obbyColor, bedrockColor, voidColor);
        this.offerListeners(new ListenerRender(this), new ListenerTick(this));
    }

    protected Color getBedrockColor() {
        return bedrockColor.getColor();
    }

    protected Color getObbyColor() {
        return obbyColor.getColor();
    }

    protected Color getVoidColor() {
        return voidColor.getColor();
    }

    protected List<BlockPos> calcVoid() {
        BlockPos playerPos = PositionUtil.getPosition();
        int r = range.getValue();
        List<BlockPos> positions = new ArrayList<>();
        int cx = playerPos.getX();
        int cz = playerPos.getZ();
        if (mc.world != null && mc.player != null) {
            for (int x = cx - r; x <= cx + r; x++) {
                for (int z = cz - r; z <= cz + r; z++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z);
                    if (dist < r * r) {
                        BlockPos pos = new BlockPos(x, mc.world.getBottomY(), z);
                        /*if (!mc.world.isAir(pos.up())) {
                            continue;
                        }*/
                        if (mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
                            positions.add(pos);
                        }
                    }
                    positions.sort(Comparator.comparingDouble(pos -> mc.player.squaredDistanceTo(Vec3d.of(pos))));
                }
            }
        }
        return positions;
    }
}
