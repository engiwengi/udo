package net.urod.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.urod.UltraRichOreDeposits;
import net.urod.block.ModBlocks;

@SuppressWarnings("unused")
public class ModItems {
    public static final Item ULTRA_COAL_ORE = ModItems.register("ultra_rich_coal_ore", new BlockItem(ModBlocks.ULTRA_COAL_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item ULTRA_IRON_ORE = ModItems.register("ultra_rich_iron_ore", new BlockItem(ModBlocks.ULTRA_IRON_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item ULTRA_DIAMOND_ORE = ModItems.register("ultra_rich_diamond_ore", new BlockItem(ModBlocks.ULTRA_DIAMOND_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item ULTRA_GOLD_ORE = ModItems.register("ultra_rich_gold_ore", new BlockItem(ModBlocks.ULTRA_GOLD_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item ULTRA_REDSTONE_ORE = ModItems.register("ultra_rich_redstone_ore", new BlockItem(ModBlocks.ULTRA_REDSTONE_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item ULTRA_LAPIS_ORE = ModItems.register("ultra_rich_lapis_ore", new BlockItem(ModBlocks.ULTRA_LAPIS_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));

    public static final Item SOIL_SAMPLER = ModItems.register("soil_sampler", new SoilSamplerItem(new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));

    public ModItems() {
        UltraRichOreDeposits.getLogger().info(String.format("Registering %s Items", UltraRichOreDeposits.MOD_ID));
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(UltraRichOreDeposits.MOD_ID, id), item);
    }
}
