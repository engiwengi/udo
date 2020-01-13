package net.urod.util;

import net.minecraft.block.Block;
import net.minecraft.util.StringIdentifiable;
import net.urod.config.QualityAmountConfig;

import java.util.concurrent.ThreadLocalRandom;

public enum OreQuality implements StringIdentifiable {
    POOR("poor"), MEDIUM("average"), HIGH("high"), ULTRA("ultra_high");

    private final String name;

    OreQuality(String name) {
        this.name = name;
    }

    public static OreQuality getRandomly() {
        int randInt = ThreadLocalRandom.current().nextInt(100);
        if (randInt < 40) {
            return OreQuality.POOR;
        } else if (randInt < 75) {
            return OreQuality.MEDIUM;
        } else if (randInt < 95) {
            return OreQuality.HIGH;
        } else {
            return OreQuality.ULTRA;
        }
    }

    public int getNewQuantity(Block block) {
        QualityAmountConfig.Range range = QualityAmountConfig.getRangeForOreQuality(block, this);
        return ThreadLocalRandom.current().nextInt(range.getLower(), range.getUpper());
    }

    @Override
    public String asString() {
        return name;
    }
}
