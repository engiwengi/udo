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

    public static Range getRangeForBlockQuality(Block block, Quality quality) {
        return map.get(block).get(quality);
    }

    private static Map<Block, Map<Quality, Range>> getDefault() {
        Map<Block, Map<Quality, Range>> newMap = Maps.newHashMap();
        Map<Quality, Range> coalMap = Maps.newHashMap();
        coalMap.put(Quality.POOR, new Range(16, 32));
        coalMap.put(Quality.MEDIUM, new Range(32, 64));
        coalMap.put(Quality.HIGH, new Range(64, 128));
        coalMap.put(Quality.ULTRA, new Range(128, 256));
        newMap.put(ModBlocks.ULTRA_COAL_ORE, coalMap);
        return newMap;
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
