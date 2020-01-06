package net.urod.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.IWorld;
import net.urod.UltraRichOreDeposits;
import net.urod.block.UltraRichOreBlock;

import java.util.Objects;

public class UltraRichOreBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    private int quantity = -1;
    private int attackTimes = 0;
    private int j = 0;

    public UltraRichOreBlockEntity() {
        super(ModBlockEntities.ULTRA_RICH_ORE);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("quantity", quantity);
        return tag;
    }

    public void onAttack(ServerWorld serverWorld, PlayerEntity player, BlockState state) {
        boolean shouldFakeBreak = false;
        attackTimes += player.getBlockBreakingSpeed(state);
        UltraRichOreDeposits.getLogger().info("mined");
        if (attackTimes > 30) {
            UltraRichOreDeposits.getLogger().info("got item");
            attackTimes = 0;
            shouldFakeBreak = decrement();
        }

        if (shouldFakeBreak) {
            serverWorld.playLevelEvent(2001, pos, Block.getRawIdFromState(state));
            Block.dropStacks(state, serverWorld, pos);
        }
        sync();
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        quantity = (tag.contains("quantity")) ? tag.getInt("quantity") : -1;
    }

    private boolean decrement() {
        markDirty();
        Objects.requireNonNull(world, String.format("decrement called on %s before world was set", toString()));
        if (quantity < 0) {
            initQuantity(world);
        }
        quantity--;
        markDirty();
        if (quantity == 0) {
            onEmpty(world);
            return false;
        } else {
            return true;
        }
    }

    private void onEmpty(IWorld iWorld) {
        iWorld.breakBlock(pos, false);
    }

    private void initQuantity(IWorld iWorld) {
        BlockState state = iWorld.getBlockState(pos);
        quantity = state.get(UltraRichOreBlock.QUALITY).getNewQuantity(state.getBlock());
        markDirty();
    }

    public float calcBlockBreakingDelta() {
        return 1.0F / quantity;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        fromTag(compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        return toTag(compoundTag);
    }

    // TODO: instead of calling onattack over and over, schedule ticks until stop mining
}
