package net.urod.mixin;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.urod.UltraRichOreDeposits;
import net.urod.block.UltraRichOreBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Shadow
    public ServerWorld world;

    @Inject(at = @At(value = "FIELD", opcode = 3, ordinal = 4), method = "continueMining")
    private void onContinueMining(BlockState blockState, BlockPos blockPos, int i, CallbackInfoReturnable<Float> cir) {
        UltraRichOreDeposits.getLogger().info("triggered mixin");
        Block block = blockState.getBlock();
        if (block instanceof UltraRichOreBlock) {
            ((UltraRichOreBlock) block).onBreaking(player, blockState, world, blockPos);
        }
    }

    //TODO: create callbacks for start and stop mining instead
}
