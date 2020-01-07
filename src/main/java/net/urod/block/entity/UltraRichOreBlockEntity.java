package net.urod.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.urod.UltraRichOreDeposits;
import net.urod.block.UltraRichOreBlock;

import java.util.Objects;

public class UltraRichOreBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    private final static double progressNeeded = 100;
    private final static int decrementAmount = 4;
    private int quantity = Integer.MIN_VALUE;
    private double progress = 30;
    private boolean isBeingMined = false;
    private PlayerEntity minedBy = null;
    private int whileMinedQuantity;
    private Direction attackDirection;

    public UltraRichOreBlockEntity() {
        super(ModBlockEntities.ULTRA_RICH_ORE);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("quantity", quantity);
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        quantity = (tag.contains("quantity")) ? tag.getInt("quantity") : -1;
    }

    public boolean decrement(ServerWorld serverWorld) {
        return decrement(serverWorld, 1);
    }

    public boolean decrement(ServerWorld serverWorld, int amount) {
        if (world.isClient) {
            UltraRichOreDeposits.getLogger().error("called decrement with the clientworld");
        }
        whileMinedQuantity -= amount;
        if (whileMinedQuantity <= 0) {
            quantity = 0;
            sync();
            serverWorld.breakBlock(pos, false, minedBy);
            return false;
        } else {
            return true;
        }
    }

    private int initQuantity(ServerWorld serverWorld) {
        markDirty();
        BlockState state = serverWorld.getBlockState(pos);
        quantity = state.get(UltraRichOreBlock.QUALITY).getNewQuantity(state.getBlock());
        return quantity;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        fromTag(compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        return toTag(compoundTag);
    }

    public void onAttackInteraction(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, PlayerEntity playerEntity, Direction direction) {
        if (world.isClient) {
            UltraRichOreDeposits.getLogger().error("called onAttackInteraction with the clientworld");
        }

        attackDirection = direction;

        if (isBeingMined || !playerEntity.isUsingEffectiveTool(blockState)) {
            if (playerEntity == minedBy) {
                isBeingMined = false;
                minedBy = null;
                UltraRichOreDeposits.getLogger().info(String.format("syncing after stop mining"));
                markDirty();
                sync();
            }
        } else {
            isBeingMined = true;
            whileMinedQuantity = getQuantity();
            minedBy = playerEntity;
            serverWorld.getBlockTickScheduler().schedule(blockPos, blockState.getBlock(), 1);
            UltraRichOreDeposits.getLogger().info(String.format("syncing on start mining"));
            sync();
        }
    }

    public void onAttackedTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos) {
        if (isBeingMined) {
            boolean shouldFakeBreak = false;
            progress += Math.sqrt(minedBy.getBlockBreakingSpeed(blockState));
            if (progress > progressNeeded) {
                progress = 0;
                shouldFakeBreak = decrement(serverWorld, decrementAmount);
            }

            if (shouldFakeBreak) {
                serverWorld.playLevelEvent(2001, blockPos, Block.getRawIdFromState(blockState));
                UltraRichOreBlock.dropStacks(blockState, serverWorld, blockPos, this, minedBy, minedBy.getMainHandStack(), attackDirection);
                minedBy.getMainHandStack().postMine(serverWorld, blockState, blockPos, minedBy);
            }
            serverWorld.getBlockTickScheduler().schedule(blockPos, blockState.getBlock(), 1);
        } else {
            progress = 0;
            quantity = whileMinedQuantity;
            markDirty();
            UltraRichOreDeposits.getLogger().info(String.format("syncing after stop mining on tick"));
            sync();
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

    private boolean hasQuantity() {
        return quantity != Integer.MIN_VALUE;
    }

    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world) {
        if (world instanceof ServerWorld || hasQuantity()) {
            double quantityPerTick = Double.min(Math.sqrt(player.getBlockBreakingSpeed(state)), progressNeeded) / progressNeeded;
            return (float) quantityPerTick / ((float) getQuantity() / decrementAmount);
        } else {
            return 0.0F;
        }
    }
}
