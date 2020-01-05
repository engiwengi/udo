package net.urod.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.urod.UltraRichOreDeposits;

@SuppressWarnings("unused")
public class ModBlocks {
    public static final Block ULTRA_COAL_ORE = ModBlocks.register("ultra_rich_coal_ore", new UltraRichOreBlock(FabricBlockSettings.of(Material.STONE).strength(2.5F, 100).build()));
    public static final Block ULTRA_IRON_ORE = ModBlocks.register("ultra_rich_iron_ore", new UltraRichOreBlock(FabricBlockSettings.copy(Blocks.IRON_ORE).strength(35, 100).build()));
    public static final Block ULTRA_GOLD_ORE = ModBlocks.register("ultra_rich_gold_ore", new UltraRichOreBlock(FabricBlockSettings.copy(Blocks.GOLD_ORE).strength(45, 100).build()));
    public static final Block ULTRA_DIAMOND_ORE = ModBlocks.register("ultra_rich_diamond_ore", new UltraRichOreBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE).strength(55, 100).build()));
    public static final Block ULTRA_REDSTONE_ORE = ModBlocks.register("ultra_rich_redstone_ore", new UltraRichOreBlock(FabricBlockSettings.copy(Blocks.REDSTONE_ORE).strength(45, 100).build()));
    public static final Block ULTRA_LAPIS_ORE = ModBlocks.register("ultra_rich_lapis_ore", new UltraRichOreBlock(FabricBlockSettings.copy(Blocks.LAPIS_ORE).strength(45, 100).build()));
    public static final Block ULTRA_EMERALD_ORE = ModBlocks.register("ultra_rich_emerald_ore", new UltraRichOreBlock(FabricBlockSettings.copy(Blocks.EMERALD_ORE).strength(55, 100).build()));

    public ModBlocks() {
        UltraRichOreDeposits.getLogger().info(String.format("Registering %s Blocks", UltraRichOreDeposits.MOD_ID));
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(UltraRichOreDeposits.MOD_ID, id), block);
    }


}
