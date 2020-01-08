package net.urod.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.packet.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.urod.api.block.BlockStopBreakingAware;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPlayerInteractionManager.class)
abstract class MixinClientPlayerInteractionManager {
    @Shadow
    @Final
    private MinecraftClient client;

    @Redirect(method = "isCurrentlyBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;areTagsEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean redirectAreTagsEqual(ItemStack left, ItemStack right) {
        return true;
    }

    @Inject(method = "sendPlayerAction", at = @At("HEAD"))
    private void onSendPlayerAction(PlayerActionC2SPacket.Action action, BlockPos blockPos, Direction direction, CallbackInfo ci) {
        World world = client.world;
        if (action == PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK || action == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
            BlockState state = Objects.requireNonNull(world).getBlockState(blockPos);
            Block block = state.getBlock();
            if (state.getBlock() instanceof BlockStopBreakingAware) {
                if (action == PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK) {
                    ((BlockStopBreakingAware) block).onBlockBreakAbort(state, world, blockPos, client.player);
                } else {
                    ((BlockStopBreakingAware) block).onBlockBreakStop(state, world, blockPos, client.player);
                }
            }
        }
    }
}
