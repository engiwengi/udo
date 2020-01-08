package net.urod.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.urod.block.entity.MinerBlockEntity;
import net.urod.block.entity.UltraRichOreBlockEntity;

public class MinerBlock extends BlockWithEntity {
    private final Stage stage;

    public MinerBlock(Stage stage, Settings settings) {
        super(settings);
        this.stage = stage;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new MinerBlockEntity();
    }

    public int getMiningSpeed() {
        return stage.getMiningSpeed();
    }

    public int getUpgradeSlots() {
        return stage.getUpgradeSlots();
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof MinerBlockEntity) {
            BlockEntity oreBlockEntity = world.getBlockEntity(pos.down());
            if (oreBlockEntity instanceof UltraRichOreBlockEntity) {
                ((MinerBlockEntity) be).onPlaced(world, pos, state, placer, itemStack, (UltraRichOreBlockEntity) oreBlockEntity);
            }
        }
    }


    public enum Stage {
        PRIMITIVE(1, 0), NORMAL(2, 1), ADVANCED(4, 2), ULTRA(8, 3);

        private int miningSpeed;
        private int upgradeSlots;

        Stage(int miningSpeed, int upgradeSlots) {
            this.miningSpeed = miningSpeed;
            this.upgradeSlots = upgradeSlots;
        }

        private int getMiningSpeed() {
            return miningSpeed;
        }

        private int getUpgradeSlots() {
            return upgradeSlots;
        }
    }
}
