package net.urod.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.urod.UltraRichOreDeposits;
import net.urod.block.ModBlocks;

public class ModBlockEntities {
    public static final BlockEntityType<UltraRichOreBlockEntity> ULTRA_RICH_ORE = ModBlockEntities.register("ultra_rich_ore",
            BlockEntityType.Builder.create(UltraRichOreBlockEntity::new, ModBlocks.ULTRA_COAL_ORE, ModBlocks.ULTRA_DIAMOND_ORE).build(null));

    public ModBlockEntities() {
        UltraRichOreDeposits.getLogger().info(String.format("Registering %s Block Entites", UltraRichOreDeposits.MOD_ID));
    }

    private static BlockEntityType<UltraRichOreBlockEntity> register(String id, BlockEntityType<UltraRichOreBlockEntity> block) {
        return Registry.register(Registry.BLOCK_ENTITY, new Identifier(UltraRichOreDeposits.MOD_ID, id), block);
    }

}
