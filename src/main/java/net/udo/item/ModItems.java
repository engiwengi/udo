package net.udo.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.udo.UltraDenseOre;
import net.udo.block.ModBlocks;

@SuppressWarnings("unused")
public class ModItems {
    public static final Item UD_COAL_ORE = register("ultra_dense_coal_ore", new BlockItem(ModBlocks.UD_COAL_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item UD_IRON_ORE = register("ultra_dense_iron_ore", new BlockItem(ModBlocks.UD_IRON_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item UD_DIAMOND_ORE = register("ultra_dense_diamond_ore", new BlockItem(ModBlocks.UD_DIAMOND_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item UD_GOLD_ORE = register("ultra_dense_gold_ore", new BlockItem(ModBlocks.UD_GOLD_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item UD_REDSTONE_ORE = register("ultra_dense_redstone_ore", new BlockItem(ModBlocks.UD_REDSTONE_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item UD_LAPIS_ORE = register("ultra_dense_lapis_ore", new BlockItem(ModBlocks.UD_LAPIS_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(UltraDenseOre.MOD_ID, id), item);
    }
}
