package net.urod.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.urod.UltraRichOreDeposits;
import net.urod.block.UltraRichOreBlock;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;

public class UltraRichOreBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    private final static double progressNeeded = 100;
    private final static int decrementAmount = 5;
    private int quantity = Integer.MIN_VALUE;
    private double progress = 0;
    private boolean isBeingMined = false;
    private PlayerEntity minedBy = null;
    private int whileMinedQuantity;

    public UltraRichOreBlockEntity() {
        super(ModBlockEntities.ULTRA_RICH_ORE);
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        fromTag(compoundTag);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        quantity = (tag.contains("quantity")) ? tag.getInt("quantity") : Integer.MIN_VALUE;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("quantity", quantity);
        return tag;
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        return toTag(compoundTag);
    }

    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (!isBeingMined && minedBy == null && player.isUsingEffectiveTool(state)) {
            UltraRichOreDeposits.getLogger().info("just started");
            isBeingMined = true;
            whileMinedQuantity = getQuantity();
            minedBy = player;
            sync();
            tickAgain(state.getBlock());
        }
    }

    private int getQuantity() {
        Objects.requireNonNull(world);
        boolean bl = quantity == Integer.MIN_VALUE;
        if (world.isClient() && bl) {
            throw new IllegalStateException("Tried to get quantity before it has been synced!");
        }
        return bl ? initQuantity((ServerWorld) world) : quantity;
    }

    private void tickAgain(Block block) {
        if (world instanceof ServerWorld) {
            world.getBlockTickScheduler().schedule(pos, block, 1);
        }
    }

    private int initQuantity(ServerWorld serverWorld) {
        markDirty();
        BlockState state = serverWorld.getBlockState(pos);
        quantity = state.get(UltraRichOreBlock.QUALITY).getNewQuantity(state.getBlock());
        return quantity;
    }

    public void onAttackedTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos) {
        if (isBeingMined) {
            boolean shouldFakeBreak = false;
            progress += Math.sqrt(minedBy.getBlockBreakingSpeed(blockState));
            if (progress > progressNeeded) {
                progress = 0;
                shouldFakeBreak = !decrementWhileMining(serverWorld, decrementAmount);
            }
            if (shouldFakeBreak) {
                serverWorld.playLevelEvent(2001, blockPos, Block.getRawIdFromState(blockState));
                UltraRichOreBlock.dropStacks(blockState, serverWorld, blockPos, this, minedBy, minedBy.getMainHandStack(), Direction.UP);
                minedBy.getMainHandStack().postMine(serverWorld, blockState, blockPos, minedBy);
            }
            tickAgain(blockState.getBlock());
        }
    }

    // Returns true if decrementing broke the block
    public boolean decrementWhileMining(ServerWorld serverWorld, int amount) {
        whileMinedQuantity -= amount;
        if (whileMinedQuantity <= 0) {
            quantity = 0;
            serverWorld.breakBlock(pos, false, minedBy);
            return true;
        } else {
            return false;
        }
    }

    public List<ItemStack> onMachineTick(ServerWorld serverWorld, int speed) {
        boolean shouldFakeBreak = false;
        progress += speed;
        if (progress > progressNeeded) {
            progress = 0;
            shouldFakeBreak = !decrementWhileMachine(serverWorld);
        }
        if (shouldFakeBreak) {
            serverWorld.playLevelEvent(2001, pos, Block.getRawIdFromState(getCachedState()));
            return UltraRichOreBlock.getDroppedStacks(getCachedState(), serverWorld, pos, this);
        } else {
            return Collections.emptyList();
        }
    }

    // Returns true if decrementing broke the block
    public boolean decrementWhileMachine(ServerWorld serverWorld) {
        quantity -= 1;
        if (quantity <= 0) {
            quantity = 0;
            breakBlockNoSound(serverWorld, pos);
            return true;
        } else {
            markDirty();
            return false;
        }
    }

    private void breakBlockNoSound(World world, BlockPos pos) {
        FluidState fluidState = world.getFluidState(pos);
        world.setBlockState(pos, fluidState.getBlockState(), 3);
    }

    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world) {
        if (world instanceof ServerWorld || hasQuantity()) {
            double quantityPerTick = Double.min(Math.sqrt(player.getBlockBreakingSpeed(state)), progressNeeded) / progressNeeded;
            return (float) quantityPerTick / ((float) getQuantity() / decrementAmount);
        } else {
            return 0.0F;
        }
    }

    private boolean hasQuantity() {
        return quantity != Integer.MIN_VALUE;
    }

    public void getNeighbours(ServerWorld serverWorld, SortedSet<BlockPos> oreBlocks, Block block) {
        if (quantity < 0) {
            getQuantity();
        }
        oreBlocks.add(pos);
        for (Direction direction : Direction.values()) {
            getNeighboursDirection(serverWorld, oreBlocks, block, direction);
        }
    }

    public void getNeighboursDirection(ServerWorld serverWorld, SortedSet<BlockPos> oreBlocks, Block block, Direction direction) {
        if (!oreBlocks.contains(pos.offset(direction))) {
            BlockEntity be = serverWorld.getBlockEntity(pos.offset(direction));
            if (be instanceof UltraRichOreBlockEntity && be.getCachedState().getBlock() == block) {
                ((UltraRichOreBlockEntity) be).getNeighbours(serverWorld, oreBlocks, block);
            }
        }
    }

    public void onBlockBreakAbort(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        onBlockBreakStop(state, world, pos, player);
    }

    public void onBlockBreakStop(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (isBeingMined) {
            if (player == minedBy) {
                UltraRichOreDeposits.getLogger().info("just canceled");
                quantity = whileMinedQuantity;
                progress = 0;
                isBeingMined = false;
                minedBy = null;
                markDirty();
                sync();
            }
        }
    }
}
