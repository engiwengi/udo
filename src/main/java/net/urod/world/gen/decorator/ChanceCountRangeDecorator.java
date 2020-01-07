package net.urod.world.gen.decorator;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.SimpleDecorator;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ChanceCountRangeDecorator extends SimpleDecorator<ChanceRangeDecoratorConfig> {
    public ChanceCountRangeDecorator(Function<Dynamic<?>, ? extends ChanceRangeDecoratorConfig> function) {
        super(function);
    }

    @Override
    public Stream<BlockPos> getPositions(Random random, ChanceRangeDecoratorConfig chanceRangeDecoratorConfig, BlockPos blockPos) {
        if (random.nextInt(chanceRangeDecoratorConfig.chance) == 0) {
            return IntStream.range(0, chanceRangeDecoratorConfig.count).mapToObj((i) -> {
                int j = random.nextInt(16) + blockPos.getX();
                int k = random.nextInt(16) + blockPos.getZ();
                int l = random.nextInt(chanceRangeDecoratorConfig.maximum - chanceRangeDecoratorConfig.topOffset) + chanceRangeDecoratorConfig.bottomOffset;
                return new BlockPos(j, l, k);
            });
        } else {
            return Stream.empty();
        }
    }
}
