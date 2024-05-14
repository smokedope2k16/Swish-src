package dev.angel.api.module;

import dev.angel.api.event.bus.Listener;
import dev.angel.api.event.events.Stage;
import dev.angel.api.util.block.BlockUtil;
import dev.angel.api.util.entity.DamageUtil;
import dev.angel.api.util.entity.EntityUtil;
import dev.angel.api.util.inventory.InventoryUtil;
import dev.angel.api.util.math.MathUtil;
import dev.angel.api.util.math.RotationsUtil;
import dev.angel.api.util.math.StopWatch;
import dev.angel.api.util.network.PacketUtil;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import dev.angel.impl.events.DeathEvent;
import dev.angel.impl.events.MotionUpdateEvent;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockEventS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.List;
/*
public class BlockPlaceModule extends Module {

    protected final Value<Boolean> rotation =
            new Value<>(
                    new String[]{"Rotation", "rotate", "rots"},
                    "Rotates to look at the block being placed.",
                    false
            );

    protected final Value<Boolean> swing =
            new Value<>(
                    new String[]{"Swing", "swingarm"},
                    "Swings your arm when placing blocks.",
                    false
            );

    protected final NumberValue<Integer> delay =
            new NumberValue<>(
                    new String[]{"Delay", "Interval"},
                    "Delay between placing blocks.",
                    3, 0, 10
            );

    protected final NumberValue<Integer> blocks =
            new NumberValue<>(
                    new String[]{"Blocks", "BlocksPerTick", "Blocks/Tick"},
                    "The amount of blocks we place per tick.",
                    2, 1, 10
            );

    protected final Value<Boolean> strict = new Value<>(
            new String[]{"StrictDirection", "strict"},
            "Wont place blocks through walls.",
            false
    );

    protected final NumberValue<Float> strictRange = new NumberValue<>(
            new String[]{"StrictRange", "wallrange"},
            "How far will place blocks through walls.",
            3.0F, 1.0F, 5.0F, 0.1F
    );

  /*  protected final Value<Boolean> altSwap =
            new Value<>(false,
                    new String[]{"AltSwap", "AlternativeSwap", "CooldownBypass"},
                    "Uses an alternative type of swap that bypass some anticheats (2b2tpvp main)"
            );

    protected final Value<Boolean> preferWebs =
            new Value<>(
                    new String[]{"OnlyWebs", "PreferWebs", "UseWebs"},
                    "Prefers webs when filling holes.",
                    false
            ); */
/*
    public BlockPlaceModule(String label, String[] aliases, String description, Category category) {
        super(label, aliases, description, category);
        this.offerValues(rotation, swing, delay, blocks, strict, strictRange);
        this.offerListeners(listenerDeath(this), listenerDisconnect(this));
    }

    private final List<Packet<?>> packets = new ArrayList<>();
    private final StopWatch timer = new StopWatch();
    private float[] rotations;
    public double enablePosY;
    private int blocksPlaced = 0;
    private int slot = -1;

  /*  @Override
    public String getSuffix() {
        return "" + blocksPlaced;
    } */
/*
    @Override
    public void onEnable() {
        packets.clear();
        blocksPlaced = 0;
        clear();

        if (mc.world == null || mc.player == null) {
            this.disable();
            return;
        }

        enablePosY = mc.player.getY();
    }

    public void clear() {
        //Implemented by module
    }

    public void onPreEvent(List<BlockPos> blocks, MotionUpdateEvent event) {
        if (event.getStage() == Stage.PRE) {
            blocksPlaced = 0;
            placeBlocks(blocks);
            if (rotations != null) {
                //Managers.ROTATION.setRotations(rotations[0], rotations[1]);
                rotations = null;
            }
            execute();
        }
    }

    public void placeBlocks(List<BlockPos> blockList) {
        if (blockList == null || blockList.isEmpty()) {
            return;
        }

        for (BlockPos pos : blockList) {
            placeBlock(pos);
        }
    }

    public void placeBlock(BlockPos pos) {
        getSlot(false);
        if (slot == -1) {
            return;
        }

        if (!timer.passed(delay.getValue() * 50)) {
            return;
        }

        Direction facing = BlockUtil.getFacing(pos);

        //TODO: raytracer
        /*
        if (facing == null) {
            BlockPos helpingPos;
            for (EnumFacing side : EnumFacing.values()) {
                helpingPos = pos.offset(side);
                EnumFacing helpingFacing = BlockUtil.getFacing(helpingPos);
                if (helpingFacing != null) {
                    facing = side;
                    placeBlock(helpingPos.offset(helpingFacing), helpingFacing.getOpposite());
                    break;
                }
            }
        }
        */
/*
        if (facing == null) {
            return;
        }

        if (blocksPlaced >= blocks.getValue()) {
            return;
        }

        if (mc.world.getBlockState(pos.offset(facing)).isAir()) {// isReplaceable()
            return;
        }

        //if ((!(this instanceof HoleFill || this instanceof InstantWeb))) {
        if (crystalCheck(pos)) {
            timer.reset();
            return;
        }
//        }

        if (canPlaceBlock(pos, strict.getValue())) {
            placeBlock(pos.offset(facing), facing.getOpposite());
        }
    }

    private void placeBlock(BlockPos pos, Direction facing) {
        float[] rots = RotationsUtil.getRotations(pos, facing);
        RaycastContext result = RaytraceUtil.getRayTraceResult(rots[0], rots[1]);
        placeBlock(pos, facing, rots, result.hitVec);
    }

    private void placeBlock(BlockPos on, Direction facing, float[] helpingRotations, Vec3d hitVec) {
        if (rotation.getValue()) {
            if (rotations == null) {
                rotations = helpingRotations;
            }
            packets.add(new EntityS2CPacket.Rotate(mc.player.getId(), (byte) helpingRotations[0], (byte) helpingRotations[1], mc.player.isOnGround()));
        }

        float[] hitRots = RaytraceUtil.hitVecToPlaceVec(on, hitVec);
        final BlockHitResult hit = new BlockHitResult(on, facing, on)
        packets.add(new PlayerInteractBlockC2SPacket(on, facing, Hand.MAIN_HAND, hitRots[0], hitRots[1], hitRots[2]));

        if (swing.getValue()) {
            packets.add(new HandSwingC2SPacket(Hand.MAIN_HAND));
        }

        blocksPlaced++;
    }

    private void execute() {
        if (!packets.isEmpty()) {
            int lastSlot = mc.player.getInventory().selectedSlot;

            // ItemStack oldItem = mc.player.getMainHandStack().getItem().getDefaultStack();

            InventoryUtil.swap(slot);


            // ItemStack newItem = mc.player.getHeldItemMainhand();

            PacketUtil.sneak(true);

        /*    if (swing.getValue()) { //this is only for the animation
                EntityUtil.swingClient();
            } */
/*
            packets.forEach(PacketUtil::send);

            packets.clear();
            timer.reset();

            PacketUtil.sneak(false);

        /*    if (altSwap.getValue() && (this instanceof HoleFill)) {
                short id = mc.player.openContainer.getNextTransactionID(mc.player.inventory);
                ItemStack fakeStack = new ItemStack(Items.END_CRYSTAL, 64);
                int newSlot = ItemUtil.hotbarToInventory(slot);
                int altSlot = ItemUtil.hotbarToInventory(lastSlot);
                Slot currentSlot = mc.player.inventoryContainer.inventorySlots.get(altSlot);
                Slot swapSlot = mc.player.inventoryContainer.inventorySlots.get(newSlot);
                PacketUtil.send(new CPacketClickWindow(0, newSlot, mc.player.inventory.currentItem, ClickType.SWAP, fakeStack, id));
                currentSlot.putStack(oldItem);
                swapSlot.putStack(newItem);
            } else { */
/*
            InventoryUtil.swap(lastSlot);
        }
    }

    private boolean crystalCheck(BlockPos pos) {
        PlayerInteractEntityC2SPacket attackPacket = null;
        float currentDmg = Float.MAX_VALUE;
        float[] angles = null;
        for (Entity entity : mc.world.getEntitiesByClass(Entity.class, new Box(pos), Entity::isAlive)) {// ???
            if (entity == null || !entity.isAlive()) {
                continue;
            }

            if (entity instanceof EndCrystalEntity) {
                double damage = EntityUtil.crystalDamage(mc.player, entity.getPos(), false, null, false);
                if (damage < currentDmg) {
                    currentDmg = (float) damage;
                    angles = RotationUtil.getRotations(entity.getX(), entity.getY(), entity.getZ());
                    attackPacket = new CPacketUseEntity(entity);
                }
            }
        }

        if (attackPacket == null) {
            return false;
        }

        int weaknessSlot = -1;
        int oldSlot = mc.player.getInventory().selectedSlot;
        if (!DamageUtil.canBreakWeakness(true)) {
            if ((weaknessSlot = DamageUtil.findAntiWeakness()) == -1) {
                return true;
            }
        }

        if (weaknessSlot != -1) {
            if (rotation.getValue()) {
                if (rotations == null) {
                    rotations = angles;
                }

                PacketUtil.send(new EntityS2CPacket.Rotate(mc.player.getId(), (byte) angles[0], (byte) angles[1], mc.player.isOnGround()));
            }

            InventoryUtil.swap(weaknessSlot);

            PacketUtil.send(attackPacket);

            if (swing.getValue()) {
                mc.player.swingHand(Hand.MAIN_HAND);
            }

            InventoryUtil.swap(oldSlot);

            return false;
        }

        if (rotation.getValue()) {
            if (rotations == null) {
                rotations = angles;
            }

            packets.add(new EntityS2CPacket.Rotate(mc.player.getId(), (byte) angles[0], (byte) angles[1], mc.player.isOnGround()));
        }

        packets.add(attackPacket);

        if (swing.getValue()) {
            packets.add(new HandSwingC2SPacket(Hand.MAIN_HAND));
        }

        return false;
    }

    private boolean canPlaceBlock(BlockPos pos, boolean strict) {
        Block block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof AirBlock /*block instanceof BlockLiquid */
/*
|| block instanceof TallPlantBlock || block instanceof FireBlock || block instanceof DeadBushBlock || block instanceof SnowBlock)) {
            return false;
        }
        //if (!(this instanceof InstantWeb)) {
            for (Entity entity : mc.world.getEntitiesByClass(Entity.class, new Box(pos), Entity::isAlive)) {
                if (entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof ArrowEntity || entity instanceof EndCrystalEntity)
                    continue;
                return false;
            }
       // }

        for (Direction side : getPlacableFacings(pos, strict)) {
            if (!canClick(pos.offset(side))) continue;
            return true;
        }

        return false;
    }

    private boolean canClick(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock().canMobSpawnInside(mc.world.getBlockState(pos));
    }

    private List<Direction> getPlacableFacings(BlockPos pos, boolean strict) {
        ArrayList<Direction> validFacings = new ArrayList<>();
        for (Direction side : Direction.values()) {
            if (strict && mc.player.squaredDistanceTo(Vec3d.of(pos)) > MathUtil.square(strictRange.getValue())) {
                Vec3d testVec = new Vec3d(pos).add(0.5, 0.5, 0.5).add(new Vec3d(side.getDirection()).scale(0.5)); //TODO: this can be more accurate
                BlockHitResult result = mc.world.raycastBlock(mc.player.getEyePos().toVector3f(), testVec);
                if (result != null && result.getType() != BlockHitResult.Type.MISS) {
                    continue;
                }
            }
            BlockPos neighbour = pos.offset(side);
            BlockState blockState = mc.world.getBlockState(neighbour);
            if ((!blockState.getBlock().canMobSpawnInside(blockState))) {
                continue;
            }

            validFacings.add(side);
        }
        return validFacings;
    }

    private void getSlot(boolean echest) {
  /*      if (this instanceof HoleFill && preferWebs.getValue() || this instanceof InstantWeb) {
            this.slot = ItemUtil.getBlockFromHotbar(Blocks.WEB);
        } else { */
/*
            this.slot = InventoryUtil.getBlockFromHotbar(Blocks.OBSIDIAN);
            if (this.slot == -1 && echest) {
                this.slot = InventoryUtil.getBlockFromHotbar(Blocks.ENDER_CHEST);
            }
        }
    }

    private Listener<?> listenerDeath(Module module) {
        return new Listener<>(DeathEvent.class) {
            @Override
            public void call(DeathEvent event) {
                if (event.getEntity() != null && event.getEntity().equals(mc.player) /*&& BlocksManager.get().disableOnDeath() */
/*
) {
                    module.disable();
                }
            }
        };
    }

    private Listener<?> listenerDisconnect(Module module) {
        return new Listener<DisconnectEvent>(DisconnectEvent.class) {
            @Override
            public void call(DisconnectEvent event) {
               // if (BlocksManager.get().disableOnDisconnect()) {
                    module.disable();
                }
           // }
        };
    }
}

 */