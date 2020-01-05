package net.urod.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.urod.UltraRichOreDeposits;
import net.urod.block.ModBlocks;

public class BlockEntities {
    public static final BlockEntityType<UltraRichOreBlockEntity> ULTRA_COAL_ORE = BlockEntities.register("ultra_rich_coal_ore",
            BlockEntityType.Builder.create(UltraRichOreBlockEntity::new, ModBlocks.ULTRA_COAL_ORE).build(null));

    public BlockEntities() {
        UltraRichOreDeposits.getLogger().info(String.format("Registering %s Block Entites", UltraRichOreDeposits.MOD_ID));
    }

    private static BlockEntityType<UltraRichOreBlockEntity> register(String id, BlockEntityType<UltraRichOreBlockEntity> block) {
        return Registry.register(Registry.BLOCK_ENTITY, new Identifier(UltraRichOreDeposits.MOD_ID, id), block);
    }

}
