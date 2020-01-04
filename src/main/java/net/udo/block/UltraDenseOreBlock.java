package net.udo.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class UltraDenseOreBlock extends OreBlock {
    private static final int maxQuantity = 10;
    private static final IntProperty QUANTITY = IntProperty.of("quantity", 0, maxQuantity);

    UltraDenseOreBlock(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()));
    }

    @Override
    public void onBroken(IWorld world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        int newQuantity = state.get(QUANTITY) - 1;
        if (newQuantity > 0) world.setBlockState(pos, state.with(QUANTITY, newQuantity), 16);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(QUANTITY);
    }
}
