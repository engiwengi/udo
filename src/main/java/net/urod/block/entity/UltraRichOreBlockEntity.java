package net.urod.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class UltraRichOreBlockEntity extends BlockEntity {
    private boolean hasCheckedNeighbours = false;
    private BlockPos masterPos = new BlockPos(0, 256, 0); // 256 is invalid height so it's just an invalid pos
    private boolean isMaster, hasMaster;

    public UltraRichOreBlockEntity() {
        super(BlockEntities.ULTRA_COAL_ORE);
    }

    @Override
    public void setWorld(World world, BlockPos blockPos) {
        super.setWorld(world, blockPos);
    }

    private List<BlockEntity> getNeighbours(IWorld iWorld) {
        List<BlockEntity> neighbours = new ArrayList<>();
        neighbours.add(iWorld.getBlockEntity(pos.up()));
        neighbours.add(iWorld.getBlockEntity(pos.down()));
        neighbours.add(iWorld.getBlockEntity(pos.north()));
        neighbours.add(iWorld.getBlockEntity(pos.east()));
        neighbours.add(iWorld.getBlockEntity(pos.south()));
        neighbours.add(iWorld.getBlockEntity(pos.west()));
        return neighbours;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putLong("masterPos", masterPos.asLong());
        tag.putBoolean("isMaster", isMaster);
        tag.putBoolean("hasMaster", hasMaster);
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        masterPos = BlockPos.fromLong(tag.getLong("masterPos"));
        isMaster = tag.getBoolean("isMaster");
        hasMaster = tag.getBoolean("hasMaster");
    }

    private boolean checkMaster(IWorld iWorld) {
        return iWorld.getBlockEntity(masterPos) instanceof UltraRichOreBlockEntity;
    }

    public void scheduledTick() {
        if (hasWorld() && !world.isClient()) {
            if (!hasMaster) {
                BlockPos newMasterPos = tryGetNeighboursMaster(world);
                if (newMasterPos != null) {
                    setMaster(newMasterPos);
                } else {
                    setMaster(pos);
                    isMaster = true;
                }
            }
        }
    }

    private BlockPos tryGetNeighboursMaster(IWorld iWorld) {
        hasCheckedNeighbours = true;
        if (hasMaster) {
            return masterPos;
        } else {
            BlockPos newMasterPos = null;
            for (BlockEntity neighbour : getNeighbours(iWorld)) {
                if (neighbour instanceof UltraRichOreBlockEntity) {
                    if (((UltraRichOreBlockEntity) neighbour).hasMaster()) {
                        newMasterPos = ((UltraRichOreBlockEntity) neighbour).getMasterPos();
                    } else if (!((UltraRichOreBlockEntity) neighbour).hasCheckedNeighbours()) {
                        newMasterPos = ((UltraRichOreBlockEntity) neighbour).tryGetNeighboursMaster(iWorld);
                    }

                    if (newMasterPos != null) {
                        return newMasterPos;
                    }
                }
            }
            return null;
        }
    }

    public boolean hasMaster() {
        return hasMaster;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(BlockPos newMasterPos) {
        hasMaster = true;
        masterPos = newMasterPos;
        markDirty();
    }

    public BlockPos getMasterPos() {
        return masterPos;
    }

    public boolean hasCheckedNeighbours() {
        return hasCheckedNeighbours;
    }
}
