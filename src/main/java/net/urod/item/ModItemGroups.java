package net.urod.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.urod.UltraRichOreDeposits;
import net.urod.block.ModBlocks;

public class ModItemGroups {
    static final ItemGroup ULTRA_DENSE_ORE = FabricItemGroupBuilder.build(
            new Identifier(UltraRichOreDeposits.MOD_ID, "udo"),
            () -> new ItemStack(ModBlocks.ULTRA_IRON_ORE));

    public ModItemGroups() {
        UltraRichOreDeposits.getLogger().info(String.format("Registering %s Item Groups", UltraRichOreDeposits.MOD_ID));
    }
}
