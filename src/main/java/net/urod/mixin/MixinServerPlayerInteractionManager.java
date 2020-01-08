package net.urod.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.network.packet.PlayerActionC2SPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.urod.api.block.BlockStopBreakingAware;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerInteractionManager.class)
abstract class MixinServerPlayerInteractionManager {
    @Shadow
    public ServerWorld world;

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;calcBlockBreakingDelta(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/BlockView;" +
            "Lnet/minecraft/util/math/BlockPos;)F", ordinal = 1), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onStopBreakingBlock(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, CallbackInfo ci, int j, BlockState blockState2) {
        if (blockState2 == null) {
            blockState2 = world.getBlockState(pos);
        }
        Block block = blockState2.getBlock();
        if (block instanceof BlockStopBreakingAware) {
            ((BlockStopBreakingAware) block).onBlockBreakStop(blockState2, world, pos, player);
        }
    }

    @Inject(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Ljava/util/Objects;equals(Ljava/lang/Object;Ljava/lang/Object;)Z"))
    private void onAbortBreakingBlock(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, CallbackInfo ci) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block instanceof BlockStopBreakingAware) {
            ((BlockStopBreakingAware) block).onBlockBreakAbort(blockState, world, pos, player);
        }
    }
}
