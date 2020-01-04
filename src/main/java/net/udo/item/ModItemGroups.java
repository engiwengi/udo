package net.udo.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.udo.UltraDenseOre;
import net.udo.block.ModBlocks;

public class ModItemGroups {
    static final ItemGroup ULTRA_DENSE_ORE = FabricItemGroupBuilder.build(
            new Identifier(UltraDenseOre.MOD_ID, "udo"),
            () -> new ItemStack(ModBlocks.UD_IRON_ORE));
}
