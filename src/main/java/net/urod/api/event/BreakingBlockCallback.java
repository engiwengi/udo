package net.urod.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;

public interface BreakingBlockCallback {
    Event<BreakingBlockCallback> EVENT = EventFactory.createArrayBacked(BreakingBlockCallback.class,
            (listeners) -> (player, block) -> {
                for (BreakingBlockCallback event : listeners) {
                    event.interact(player, block);
                }
            });

    void interact(PlayerEntity player, Block block);
}
