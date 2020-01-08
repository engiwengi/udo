package net.urod.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface BlockStopBreakingAware<T extends Block> {
    /**
     * Fired on the client right before sending a packet to tell the server to stop breaking the block at pos
     * <p>
     * Fired on the server right before the player stops breaking a block to destroy it
     * <p>
     * Doesn't fire on blocks that return true for Block#isAir()
     * <p>
     * Stop breaking means to finish breaking a block
     */
    void onBlockBreakStop(BlockState state, World world, BlockPos pos, PlayerEntity player);

    /**
     * Fired on the client right before sending a packet to tell the server to abort breaking the block
     * <p>
     * Fired on the server when it is determined that the player aborted breaking the block
     * <p>
     * Abort breaking means to not finish breaking the block e.g changing ItemStack or looking away
     */
    void onBlockBreakAbort(BlockState state, World world, BlockPos pos, PlayerEntity player);
}
