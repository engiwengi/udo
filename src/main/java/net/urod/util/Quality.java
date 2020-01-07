package net.urod.util;

import net.minecraft.block.Block;
import net.minecraft.util.StringIdentifiable;
import net.urod.config.QualityAmountConfig;

import java.util.concurrent.ThreadLocalRandom;

public enum Quality implements StringIdentifiable {
    POOR("poor") {
    },
    MEDIUM("average") {
    },
    HIGH("high") {
    },
    ULTRA("ultra_high") {
    };

    private final String name;

    Quality(String name) {
        this.name = name;
    }

    public static Quality getRandomly() {
        int randInt = ThreadLocalRandom.current().nextInt(100);
        if (randInt < 40) {
            return Quality.POOR;
        } else if (randInt < 75) {
            return Quality.MEDIUM;
        } else if (randInt < 95) {
            return Quality.HIGH;
        } else {
            return Quality.ULTRA;
        }
    }

    public int getNewQuantity(Block block) {
        QualityAmountConfig.Range range = QualityAmountConfig.getRangeForBlockQuality(block, this);
        return ThreadLocalRandom.current().nextInt(range.getLower(), range.getUpper());
    }

    @Override
    public String asString() {
        return name;
    }
}
