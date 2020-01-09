package net.urod.block.entity;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.urod.block.MinerBlock;
import net.urod.util.BlockPosWithDistFromMiner;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class MinerBlockEntity extends BlockEntity implements Tickable {
    private BlockPos masterPos;
    private TreeSet<BlockPosWithDistFromMiner> oreBlocks;
    private BlockPos workingPos = BlockPos.ORIGIN;
    private UltraRichOreBlockEntity currentBlockEntity = null;
    private boolean firstTick = true;

    //TODO: clean up setWorkingPos

    public MinerBlockEntity() {
        super(ModBlockEntities.MINER);
        oreBlocks = newOreBlocks();
    }

    static public TreeSet<BlockPosWithDistFromMiner> newOreBlocks() {
        return Sets.newTreeSet(Comparator.comparingInt(BlockPosWithDistFromMiner::getD).thenComparingLong(BlockPosWithDistFromMiner::asLong));
    }

    @Override
    public void setWorld(World world, BlockPos blockPos) {
        super.setWorld(world, blockPos);
        masterPos = pos.down();
    }


    // removed for now to just init on first tick instead
    //    @Override
    //    public void fromTag(CompoundTag tag) {
    //        super.fromTag(tag);
    //        oreBlocks =
    //                Arrays.stream(tag.getLongArray("oreblocks")).mapToObj(BlockPosWithDistFromMiner::fromLong).collect(Collectors.toCollection(MinerBlockEntity::newOreBlocks));
    //        setWorkingPos(new BlockPosWithDistFromMiner(BlockPos.fromLong(tag.getLong("workingpos")), 0));
    //    }
    //
    //    @Override
    //    public CompoundTag toTag(CompoundTag tag) {
    //        super.toTag(tag);
    //        tag.putLongArray("oreblocks", oreBlocks.stream().map(BlockPosWithDistFromMiner::asLong).collect(Collectors.toList()));
    //        tag.putLong("workingpos", workingPos.asLong());
    //        return tag;
    //    }

    private void setWorkingPos(BlockPosWithDistFromMiner workingPos) {
        if (workingPos == null) {
            this.workingPos = BlockPos.ORIGIN;
        } else {
            this.workingPos = workingPos.toBlockPos();
        }
    }

    public void onPlaced(ServerWorld serverWorld, BlockPos blockPos, UltraRichOreBlockEntity oreBlockEntity) {
        onInit();
    }

    private void setOreBlocks() {
        BlockEntity be = world.getBlockEntity(masterPos);
        if (be instanceof UltraRichOreBlockEntity) {
            TreeSet<BlockPosWithDistFromMiner> tempOreBlocks = newOreBlocks();
            ((UltraRichOreBlockEntity) be).loadDeposit((ServerWorld) world, tempOreBlocks, world.getBlockState(masterPos).getBlock());
            //            ((UltraRichOreBlockEntity) be).getNeighbours((ServerWorld) world, tempOreBlocks, world.getBlockState(masterPos).getBlock());
            oreBlocks.addAll(tempOreBlocks);
        }
    }

    private void onInit() {
        if (world instanceof ServerWorld) {
            setOreBlocks();
            setWorkingPos(oreBlocks.pollLast());
            if (!trySetCurrentBEFromWorkingPos()) {
                trySetCurrentBEFromNextOreBlock();
            }
        }
    }

    private boolean trySetCurrentBEFromWorkingPos() {
        if (workingPos != BlockPos.ORIGIN) {
            if (world != null) {
                BlockEntity be = world.getBlockEntity(workingPos);
                if (be instanceof UltraRichOreBlockEntity && !be.isRemoved()) {
                    currentBlockEntity = (UltraRichOreBlockEntity) be;
                    return true;
                } else {
                    setWorkingPos(null);
                }
            }
        }
        return false;
    }

    private UltraRichOreBlockEntity getCurrentBlockEntity() {
        if (currentBlockEntity == null || currentBlockEntity.isRemoved()) {
            currentBlockEntity = null;
            if (!trySetCurrentBEFromWorkingPos() && !trySetCurrentBEFromNextOreBlock()) {
                if (currentBlockEntity != null) {
                    setWorkingPos(new BlockPosWithDistFromMiner(currentBlockEntity.getPos(), 0));
                }
            }
        }
        return currentBlockEntity;
    }

    private boolean trySetCurrentBEFromNextOreBlock() {
        BlockPosWithDistFromMiner nextPos = oreBlocks.pollLast();
        if (nextPos != null && world != null) {
            BlockEntity be = world.getBlockEntity(nextPos.toBlockPos());
            if (invalidBlockEntity(be)) {
                return trySetCurrentBEFromNextOreBlock();
            } else {
                setWorkingPos(be == null ? null : new BlockPosWithDistFromMiner(be.getPos(), 0));
                currentBlockEntity = (UltraRichOreBlockEntity) be;
                return true;
            }
        }
        return false;
    }

    private boolean invalidBlockEntity(BlockEntity be) {
        if ((world == null) || (!(be instanceof UltraRichOreBlockEntity)) || (be.isRemoved())) {
            return true;
        } else {
            BlockEntity masterBE = world.getBlockEntity(masterPos);
            if ((!(masterBE instanceof UltraRichOreBlockEntity)) || (masterBE.isRemoved())) {
                return true;
            } else {
                return masterBE.getCachedState().getBlock() != be.getCachedState().getBlock();
            }
        }
    }

    @Override
    public void tick() {
        if (firstTick == true && world instanceof ServerWorld) {
            firstTick = false;
            onInit();
        }
        if (world instanceof ServerWorld && getCurrentBlockEntity() != null) {
            List<ItemStack> itemStacks = getCurrentBlockEntity().onMachineTick((ServerWorld) world, ((MinerBlock) getCachedState().getBlock()).getMiningSpeed());
            for (ItemStack stack : itemStacks) {
                Block.dropStack(world, pos, stack);
            }
        }
    }
}
