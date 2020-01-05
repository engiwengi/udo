package net.urod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.urod.block.entity.UltraRichOreBlockEntity;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UltraRichOreBlock extends OreBlock implements BlockEntityProvider {
    private static final int maxQuantity = 10;
    private static final IntProperty QUANTITY = IntProperty.of("quantity", 0, UltraRichOreBlock.maxQuantity);

    UltraRichOreBlock(Settings settings) {
        super(settings);
        setDefaultState((stateManager.getDefaultState().with(UltraRichOreBlock.QUANTITY, 10)));
    }

    public BlockState getRandomState() {
        return getDefaultState().with(UltraRichOreBlock.QUANTITY, ThreadLocalRandom.current().nextInt((int) (UltraRichOreBlock.maxQuantity * 0.7F), UltraRichOreBlock.maxQuantity + 1));
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof UltraRichOreBlockEntity) {
            ((UltraRichOreBlockEntity) blockEntity).scheduledTick();
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getRandomState();
    }

    @Override
    public void onBroken(IWorld world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        if (world.isClient()) {
            int newQuantity = state.get(UltraRichOreBlock.QUANTITY) - 1;
            if (newQuantity > 0) {
                world.setBlockState(pos, state.with(UltraRichOreBlock.QUANTITY, newQuantity), 16);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UltraRichOreBlock.QUANTITY);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new UltraRichOreBlockEntity();
    }

    @Override
    public int getTickRate(WorldView worldView) {
        return 1;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved) {
        super.neighborUpdate(state, world, pos, block, neighborPos, moved);
        if (!world.isClient()) {
            scheduledTick(state, (ServerWorld) world, pos, new Random());
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
//        int newQuantity = state.get(UltraRichOreBlock.QUANTITY) - 1;
//        if (newQuantity > 0) {
//            world.setBlockState(pos, state.with(UltraRichOreBlock.QUANTITY, newQuantity), 16);
//        }
    }
}
