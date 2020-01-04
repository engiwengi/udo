package net.udo.mixin;


import net.minecraft.block.BlockState;
import net.minecraft.state.property.IntProperty;
import net.udo.block.UltraDenseOreBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(UltraDenseOreBlock.class)
public class UltraDenseOreBlockMixin extends BlockMixin {
    @Shadow
    @Final
    private static IntProperty QUANTITY;
    @Shadow
    @Final
    private static int maxQuantity;

    @Override
    public void onGetDefaultState(CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(cir.getReturnValue().with(QUANTITY, ThreadLocalRandom.current().nextInt((int) (maxQuantity * 0.7F), maxQuantity + 1)));
    }
}
