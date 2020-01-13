package net.urod.block.entity;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.urod.block.MinerBlock;
import net.urod.util.BlockPosWithDistFromMiner;

import java.util.*;

public class MinerBlockEntity extends BlockEntity implements Tickable {
    private TreeSet<BlockPosWithDistFromMiner> workingDeposit = emptyDeposit();
    private Optional<BlockPos> workingPos = Optional.empty();
    private Optional<UltraRichOreBlockEntity> workingBlockEntity = Optional.empty();
    private Optional<Inventory> outputInventory = Optional.empty();
    private boolean firstTick = true;

    public MinerBlockEntity() {
        super(ModBlockEntities.MINER);
    }

    static public TreeSet<BlockPosWithDistFromMiner> emptyDeposit() {
        return Sets.newTreeSet(Comparator.comparingInt(BlockPosWithDistFromMiner::getD).thenComparingLong(BlockPosWithDistFromMiner::asLong));
    }

    // I don't think saving is necessary since we can init on first tick

    //    @Override
    //    public void fromTag(CompoundTag tag) {
    //        super.fromTag(tag);
    //        oreBlocks =
    //                Arrays.stream(tag.getLongArray("oreblocks")).mapToObj(BlockPosWithDistFromMiner::fromLong).collect(Collectors.toCollection
    //                (MinerBlockEntity::newOreBlocks));
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

    private void setWorkingPos(BlockPos newWorkingPos) {
        workingPos = Optional.ofNullable(newWorkingPos);
    }


    private boolean trySetWorkingDeposit() {
        Objects.requireNonNull(world);
        BlockEntity be = world.getBlockEntity(pos.down());
        if (be instanceof UltraRichOreBlockEntity) {
            TreeSet<BlockPosWithDistFromMiner> tempOreBlocks = emptyDeposit();
            ((UltraRichOreBlockEntity) be).loadDeposit((ServerWorld) world, tempOreBlocks, world.getBlockState(pos.down()).getBlock());
            workingDeposit.addAll(tempOreBlocks);
        }
        return !workingDeposit.isEmpty();
    }

    private boolean trySetWorkingBlockEntity() {
        return (fromWorkingPos() || fromNextOreBlock());
    }

    public void onInit() {
        if (world instanceof ServerWorld) {
            if (trySetWorkingDeposit()) {
                setWorkingPos(workingDeposit.pollLast().toBlockPos());
                getWorkingBlockEntity();
            }
        }
    }

    private boolean fromWorkingPos() {
        Objects.requireNonNull(world);
        workingPos.ifPresent((blockPos -> setWorkingBlockEntityAs(world.getBlockEntity(blockPos))));
        return workingBlockEntity.isPresent();
    }

    private UltraRichOreBlockEntity getWorkingBlockEntity() {
        return workingBlockEntity.filter(this::validOreBlock).orElse(trySetWorkingBlockEntity() ? getWorkingBlockEntity() : null);
    }

    private void setWorkingBlockEntityAs(BlockEntity be) {
        workingBlockEntity =
            Optional.ofNullable(be).filter((blockEntity -> blockEntity instanceof UltraRichOreBlockEntity)).map((blockEntity -> (UltraRichOreBlockEntity) blockEntity));
        // Also reset the working pos here since the working block entity was probably changed
        setWorkingPos(workingBlockEntity.map(BlockEntity::getPos).orElse(null));
    }

    private boolean validOreBlock(BlockEntity be) {
        Objects.requireNonNull(world);
        if (((be instanceof UltraRichOreBlockEntity)) && (!be.isRemoved())) {
            BlockEntity masterBlockEntity = world.getBlockEntity(pos.down());
            return masterBlockEntity instanceof UltraRichOreBlockEntity && !masterBlockEntity.isRemoved() && masterBlockEntity.getCachedState().getBlock() == be.getCachedState().getBlock();
        }
        return false;
    }

    private boolean fromNextOreBlock() {
        Objects.requireNonNull(world);
        nextOreBlock().ifPresent((nextPos) -> setWorkingBlockEntityAs(world.getBlockEntity(nextPos)));
        // We set it and everything is gravy || No blocks left to set so give up || Try and set the next one
        return workingBlockEntity.isPresent() || workingDeposit.isEmpty() || fromNextOreBlock();
    }

    private Optional<BlockPos> nextOreBlock() {
        return Optional.ofNullable(workingDeposit.pollLast()).map(BlockPosWithDistFromMiner::toBlockPos);
    }

    @Override
    public void tick() {
        if (firstTick == true && world instanceof ServerWorld) {
            firstTick = false;
            onInit();
        }
        if (world instanceof ServerWorld && getWorkingBlockEntity() != null) {
            List<ItemStack> itemStacks = getWorkingBlockEntity().onMachineTick((ServerWorld) world,
                ((MinerBlock) getCachedState().getBlock()).getMiningSpeed());
            for (ItemStack stack : itemStacks) {
                outputInventory.ifPresent((inventory -> inventory.setInvStack()));
                Block.dropStack(world, pos, stack);
            }
        }
    }
}
