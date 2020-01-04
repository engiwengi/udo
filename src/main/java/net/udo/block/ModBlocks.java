package net.udo.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.udo.UltraDenseOre;

@SuppressWarnings("unused")
public class ModBlocks {
    public static final Block UD_COAL_ORE = register("ultra_dense_coal_ore", new UltraDenseOreBlock(FabricBlockSettings.of(Material.STONE).strength(2.5F, 100).build()));
    public static final Block UD_IRON_ORE = register("ultra_dense_iron_ore", new UltraDenseOreBlock(FabricBlockSettings.copy(Blocks.IRON_ORE).strength(35, 100).build()));
    public static final Block UD_GOLD_ORE = register("ultra_dense_gold_ore", new UltraDenseOreBlock(FabricBlockSettings.copy(Blocks.GOLD_ORE).strength(45, 100).build()));
    public static final Block UD_DIAMOND_ORE = register("ultra_dense_diamond_ore", new UltraDenseOreBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE).strength(55, 100).build()));
    public static final Block UD_REDSTONE_ORE = register("ultra_dense_redstone_ore", new UltraDenseOreBlock(FabricBlockSettings.copy(Blocks.REDSTONE_ORE).strength(45, 100).build()));
    public static final Block UD_LAPIS_ORE = register("ultra_dense_lapis_ore", new UltraDenseOreBlock(FabricBlockSettings.copy(Blocks.LAPIS_ORE).strength(45, 100).build()));
    public static final Block UD_EMERALD_ORE = register("ultra_dense_emerald_ore", new UltraDenseOreBlock(FabricBlockSettings.copy(Blocks.EMERALD_ORE).strength(55, 100).build()));

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(UltraDenseOre.MOD_ID, id), block);
    }
}
