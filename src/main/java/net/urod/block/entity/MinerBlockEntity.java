package net.urod.block.entity;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.urod.block.MinerBlock;

import java.util.*;
import java.util.stream.Collectors;

public class MinerBlockEntity extends BlockEntity implements Tickable {
    private TreeSet<BlockPos> oreBlocks;
    private UltraRichOreBlockEntity currentBlockEntity = null;

    public MinerBlockEntity() {
        super(ModBlockEntities.MINER);
        oreBlocks = Sets.newTreeSet(Comparator.<BlockPos>comparingDouble((blockPos) -> blockPos.getSquaredDistance(pos)).thenComparing(BlockPos::hashCode));
    }

    public void onPlaced(IWorld iWorld, BlockPos blockPos, BlockState blockState, LivingEntity placer, ItemStack itemStack,
                         UltraRichOreBlockEntity oreBlockEntity) {
        oreBlockEntity.getNeighbours(iWorld, oreBlocks, blockState.getBlock());
    }


    private UltraRichOreBlockEntity getCurrentBe() {
        if (currentBlockEntity == null || currentBlockEntity.isRemoved()) {
            currentBlockEntity = getNext();
        }
        return currentBlockEntity;
    }


    private UltraRichOreBlockEntity getNext() {
        Objects.requireNonNull(world);
        BlockPos nextPos = oreBlocks.pollLast();
        if (nextPos == null) {
            return null;
        }
        BlockEntity be = world.getBlockEntity(nextPos);
        if (be instanceof UltraRichOreBlockEntity) {
            if (be.isRemoved() || (be.getCachedState().getBlock() != world.getBlockState(pos.down()).getBlock())) {
                return getNext();
            } else {
                return (UltraRichOreBlockEntity) be;
            }
        } else {
            return null;
        }
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        oreBlocks = (TreeSet<BlockPos>) Arrays.stream(tag.getLongArray("oreblocks")).mapToObj(BlockPos::fromLong).collect(Collectors.toSet());
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putLongArray("oreblocks", oreBlocks.stream().map(BlockPos::asLong).collect(Collectors.toList()));
        return tag;
    }

    @Override
    public void tick() {
        if (world instanceof ServerWorld && getCurrentBe() != null) {
            List<ItemStack> itemStacks = getCurrentBe().onMachineTick((ServerWorld) world, ((MinerBlock) getCachedState().getBlock()).getMiningSpeed());
            for (ItemStack stack : itemStacks) {
                Block.dropStack(world, pos, stack);
            }
        }
    }
}
