package net.urod.util;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BlockPosWithDistFromMinerTest {

    @Test
    void fromToLong() {
        BlockPosWithDistFromMiner posWithMiner = new BlockPosWithDistFromMiner(new BlockPos(10, 10, 10), 10);
        long asLong = posWithMiner.asLong();
        BlockPosWithDistFromMiner fromLong = BlockPosWithDistFromMiner.fromLong(asLong);
        Assertions.assertEquals(posWithMiner.getD(), fromLong.getD());
        Assertions.assertEquals(posWithMiner.getX(), fromLong.getX());
        Assertions.assertEquals(posWithMiner.getY(), fromLong.getY());
        Assertions.assertEquals(posWithMiner.getZ(), fromLong.getZ());
    }

    @Test
    void fromToLongBig() {
        BlockPosWithDistFromMiner posWithMiner = new BlockPosWithDistFromMiner(new BlockPos(15000000, 244, 15000000), 63);
        long asLong = posWithMiner.asLong();
        BlockPosWithDistFromMiner fromLong = BlockPosWithDistFromMiner.fromLong(asLong);
        Assertions.assertEquals(posWithMiner.getD(), fromLong.getD());
        Assertions.assertEquals(posWithMiner.getX(), fromLong.getX());
        Assertions.assertEquals(posWithMiner.getY(), fromLong.getY());
        Assertions.assertEquals(posWithMiner.getZ(), fromLong.getZ());
    }

    @Test
    void toBlockPos() {
        BlockPosWithDistFromMiner posWithMiner = new BlockPosWithDistFromMiner(new BlockPos(15000000, 244, 15000000), 63);
        BlockPos blockPos = posWithMiner.toBlockPos();
        Assertions.assertEquals(posWithMiner.getX(), blockPos.getX());
        Assertions.assertEquals(posWithMiner.getY(), blockPos.getY());
        Assertions.assertEquals(posWithMiner.getZ(), blockPos.getZ());
    }
}