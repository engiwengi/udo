package net.urod.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.urod.UltraRichOreDeposits;
import net.urod.block.ModBlocks;

public class ModBlockEntities {
    public static final BlockEntityType<UltraRichOreBlockEntity> ULTRA_RICH_ORE = ModBlockEntities.register("ultra_rich_ore",
        BlockEntityType.Builder.create(UltraRichOreBlockEntity::new, ModBlocks.RICH_COAL_ORE).build(null));
    public static final BlockEntityType<MinerBlockEntity> MINER = ModBlockEntities.register("rich_ore_miner",
        BlockEntityType.Builder.create(MinerBlockEntity::new, ModBlocks.MINER).build(null));

    public ModBlockEntities() {
        UltraRichOreDeposits.getLogger().info(String.format("Registering %s Block Entites", UltraRichOreDeposits.MOD_ID));
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType<T> blockEntityType) {
        return Registry.register(Registry.BLOCK_ENTITY, new Identifier(UltraRichOreDeposits.MOD_ID, id), blockEntityType);
    }

}
