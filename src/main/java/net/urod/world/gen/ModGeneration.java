package net.urod.world.gen;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.urod.UltraRichOreDeposits;
import net.urod.block.ModBlocks;
import net.urod.block.UltraRichOreBlock;
import net.urod.world.gen.decorator.ChanceRangeDecoratorConfig;
import net.urod.world.gen.decorator.ModDecorators;

public class ModGeneration {
    public ModGeneration() {
        UltraRichOreDeposits.getLogger().info(String.format("Handling %s Feature Generation",
            UltraRichOreDeposits.MOD_ID));
        ModGeneration.handleDenseOreFeatureGeneration();
    }

    private static void handleDenseOreFeatureGeneration() {
        for (Biome biome : Registry.BIOME) {
            ModGeneration.handleBiome(biome);
        }
        RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> ModGeneration.handleBiome(biome));
    }

    private static void handleBiome(Biome biome) {
        handleBiomeForOre(ModBlocks.RICH_COAL_ORE, biome, 16, 1, 0, 0, 100, 16);
        handleBiomeForOre(ModBlocks.RICH_IRON_ORE, biome, 16, 1, 0, 0, 100, 16);
        handleBiomeForOre(ModBlocks.RICH_GOLD_ORE, biome, 16, 1, 0, 0, 100, 16);
        handleBiomeForOre(ModBlocks.RICH_REDSTONE_ORE, biome, 16, 1, 0, 0, 100, 16);
        handleBiomeForOre(ModBlocks.RICH_DIAMOND_ORE, biome, 16, 1, 0, 0, 100, 16);
        handleBiomeForOre(ModBlocks.RICH_LAPIS_ORE, biome, 16, 1, 0, 0, 100, 16);
    }

    private static void handleBiomeForOre(Block block, Biome biome, int size, int count, int bottomOffset,
                                          int topOffset, int maximum, int chance) {
        if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
            biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES,
                Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE,
                    ((UltraRichOreBlock) block).getRandomState(), size //Ore vein size
                )).createDecoratedFeature(new ConfiguredDecorator<>(ModDecorators.CHANCE_RANGE_COUNT,
                    new ChanceRangeDecoratorConfig(count, //Number of veins per chunk
                    bottomOffset, //Bottom Offset
                    topOffset, //Min y level
                    maximum, //Max y level
                    chance // 1 in [chance] per chunk
                )))

            );
        }
    }
}
