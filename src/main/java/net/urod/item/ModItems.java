package net.urod.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.urod.UltraRichOreDeposits;
import net.urod.block.ModBlocks;

@SuppressWarnings("unused")
public class ModItems {
    // ORES
    public static final Item RICH_COAL_ORE = ModItems.register("ultra_rich_coal_ore", new BlockItem(ModBlocks.RICH_COAL_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item RICH_IRON_ORE = ModItems.register("ultra_rich_iron_ore", new BlockItem(ModBlocks.RICH_IRON_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item RICH_DIAMOND_ORE = ModItems.register("ultra_rich_diamond_ore", new BlockItem(ModBlocks.RICH_DIAMOND_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item RICH_GOLD_ORE = ModItems.register("ultra_rich_gold_ore", new BlockItem(ModBlocks.RICH_GOLD_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item RICH_REDSTONE_ORE = ModItems.register("ultra_rich_redstone_ore", new BlockItem(ModBlocks.RICH_REDSTONE_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item RICH_LAPIS_ORE = ModItems.register("ultra_rich_lapis_ore", new BlockItem(ModBlocks.RICH_LAPIS_ORE, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));

    // MINERS
    public static final Item PRIMITIVE_MINER = ModItems.register("primitive_miner", new BlockItem(ModBlocks.PRIMITIVE_MINER, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item MINER = ModItems.register("miner", new BlockItem(ModBlocks.MINER, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item ADVANCED_MINER = ModItems.register("advanced_miner", new BlockItem(ModBlocks.ADVANCED_MINER, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));
    public static final Item ULTRA_MINER = ModItems.register("ultra_miner", new BlockItem(ModBlocks.ULTRA_MINER, new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));


    // SAMPLER
    public static final Item SOIL_SAMPLER = ModItems.register("soil_sampler", new SoilSamplerItem(new Item.Settings().group(ModItemGroups.ULTRA_DENSE_ORE)));

    public ModItems() {
        UltraRichOreDeposits.getLogger().info(String.format("Registering %s Items", UltraRichOreDeposits.MOD_ID));
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(UltraRichOreDeposits.MOD_ID, id), item);
    }
}
