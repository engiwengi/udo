package net.urod.config;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.urod.block.ModBlocks;
import net.urod.util.Quality;

import java.util.Map;

public class QualityAmountConfig {
    private static Map<Block, Map<Quality, Range>> map = getMap();

    private static Map<Block, Map<Quality, Range>> getMap() {
        return getDefault();
    }

    private static Map<Block, Map<Quality, Range>> getDefault() {
        Map<Block, Map<Quality, Range>> newMap = Maps.newHashMap();
        newMap.put(ModBlocks.RICH_COAL_ORE, resourceMapWith(8, 16, 32, 64, 128));
        newMap.put(ModBlocks.RICH_IRON_ORE, resourceMapWith(4, 8, 16, 32, 64));
        newMap.put(ModBlocks.RICH_GOLD_ORE, resourceMapWith(4, 8, 16, 32, 64));
        newMap.put(ModBlocks.RICH_REDSTONE_ORE, resourceMapWith(4, 8, 16, 32, 64));
        newMap.put(ModBlocks.RICH_DIAMOND_ORE, resourceMapWith(2, 4, 8, 16, 32));
        newMap.put(ModBlocks.RICH_LAPIS_ORE, resourceMapWith(2, 4, 8, 16, 32));
        newMap.put(ModBlocks.RICH_EMERALD_ORE, resourceMapWith(2, 4, 8, 16, 32));
        return newMap;
    }

    private static Map<Quality, Range> resourceMapWith(int poor, int medium, int high, int ultra, int max) {
        Map<Quality, Range> resourceMap = Maps.newHashMap();
        resourceMap.put(Quality.POOR, new Range(poor, medium));
        resourceMap.put(Quality.MEDIUM, new Range(medium, high));
        resourceMap.put(Quality.HIGH, new Range(high, ultra));
        resourceMap.put(Quality.ULTRA, new Range(ultra, max));
        return resourceMap;
    }

    public static Range getRangeForBlockQuality(Block block, Quality quality) {
        return map.get(block).get(quality);
    }

    public static class Range {
        private final int upper;
        private final int lower;

        private Range(int lower, int upper) {
            this.upper = upper;
            this.lower = lower;
        }

        public int getUpper() {
            return upper;
        }

        public int getLower() {
            return lower;
        }
    }
}
