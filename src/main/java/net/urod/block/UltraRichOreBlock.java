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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.urod.block.entity.UltraRichOreBlockEntity;
import net.urod.state.property.QualityProperty;
import net.urod.util.Quality;

public class UltraRichOreBlock extends OreBlock implements BlockEntityProvider {
    public static final QualityProperty QUALITY = QualityProperty.of("quantity");

    UltraRichOreBlock(Settings settings) {
        super(settings);
        setDefaultState((stateManager.getDefaultState().with(UltraRichOreBlock.QUALITY, Quality.ULTRA)));
    }

    public BlockState getRandomState() {
        return getDefaultState().with(UltraRichOreBlock.QUALITY, Quality.getRandomly());
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getRandomState();
    }

    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        if (player.isUsingEffectiveTool(state)) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof UltraRichOreBlockEntity) {
                return ((UltraRichOreBlockEntity) be).calcBlockBreakingDelta();
            } else {
                return super.calcBlockBreakingDelta(state, player, world, pos);
            }
        } else {
            return 0.005F;
        }
    }

    public void onBreaking(PlayerEntity player, BlockState state, ServerWorld serverWorld, BlockPos pos) {
        if (player.isUsingEffectiveTool(state) && !player.isCreative()) {
            BlockEntity be = serverWorld.getBlockEntity(pos);
            if (be instanceof UltraRichOreBlockEntity) {
                ((UltraRichOreBlockEntity) be).onAttack(serverWorld, player, state);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UltraRichOreBlock.QUALITY);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new UltraRichOreBlockEntity();
    }
}
