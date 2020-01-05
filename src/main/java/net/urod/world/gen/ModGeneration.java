package net.urod.world.gen;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.urod.UltraRichOreDeposits;
import net.urod.block.ModBlocks;
import net.urod.block.UltraRichOreBlock;

public class ModGeneration {
    public ModGeneration() {
        UltraRichOreDeposits.getLogger().info(String.format("Handling %s Feature Generation", UltraRichOreDeposits.MOD_ID));
        ModGeneration.handleDenseOreFeatureGeneration();
    }

    private static void handleDenseOreFeatureGeneration() {
        for (Biome biome : Registry.BIOME) {
            ModGeneration.handleBiome(biome);
        }
        RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> ModGeneration.handleBiome(biome));
    }

    private static void handleBiome(Biome biome) {
        if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
            biome.addFeature(
                    GenerationStep.Feature.UNDERGROUND_ORES,
                    Feature.ORE.configure(
                            new OreFeatureConfig(
                                    OreFeatureConfig.Target.NATURAL_STONE,
                                    ((UltraRichOreBlock) ModBlocks.ULTRA_COAL_ORE).getRandomState(),
                                    8 //Ore vein size
                            )).createDecoratedFeature(new ConfiguredDecorator<>(
                            Decorator.COUNT_RANGE,
                            new RangeDecoratorConfig(
                                    50, //Number of veins per chunk
                                    0, //Bottom Offset
                                    0, //Min y level
                                    100 //Max y level
                            ))
                    )

            );
        }
    }
}
