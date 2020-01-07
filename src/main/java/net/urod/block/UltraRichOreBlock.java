package net.urod.block;

import net.fabricmc.fabric.api.block.BlockAttackInteractionAware;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.urod.block.entity.UltraRichOreBlockEntity;
import net.urod.state.property.QualityProperty;
import net.urod.util.Quality;

import java.util.Random;

public class UltraRichOreBlock extends OreBlock implements BlockEntityProvider, BlockAttackInteractionAware {
    public static final QualityProperty QUALITY = QualityProperty.of("quality");

    UltraRichOreBlock(Settings settings) {
        super(settings);
        setDefaultState((stateManager.getDefaultState().with(UltraRichOreBlock.QUALITY, Quality.ULTRA)));
    }

    public static void dropStacks(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, Direction direction) {
        if (world instanceof ServerWorld) {
            getDroppedStacks(state, (ServerWorld) world, pos, blockEntity, entity, stack).forEach((itemStack) -> {
                dropStack(world, pos, itemStack, direction);
            });
        }

        state.onStacksDropped(world, pos, stack);
    }

    private static void dropStack(World world, BlockPos pos, ItemStack stack, Direction direction) {
        if (!world.isClient && !stack.isEmpty() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            ItemEntity itemEntity = new ItemEntity(world,
                    (double) pos.getX() + (double) direction.getOffsetX() * 0.5 + 0.5,
                    (double) pos.getY() + (double) direction.getOffsetY() * 0.5 + 0.5,
                    (double) pos.getZ() + (double) direction.getOffsetZ() * 0.5 + 0.5,
                    stack);
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        }
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
                return ((UltraRichOreBlockEntity) be).calcBlockBreakingDelta(state, player, world);
            }
        }
        return 0.005F;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof UltraRichOreBlockEntity) {
            ((UltraRichOreBlockEntity) be).onAttackedTick(state, world, pos);
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

    @Override
    public boolean onAttackInteraction(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, Direction direction) {
        if (world instanceof ServerWorld) {
            BlockEntity be = world.getBlockEntity(blockPos);
            if (be instanceof UltraRichOreBlockEntity) {
                ((UltraRichOreBlockEntity) be).onAttackInteraction(blockState, (ServerWorld) world, blockPos, playerEntity, direction);
            }
        }
        return false;
    }
}
