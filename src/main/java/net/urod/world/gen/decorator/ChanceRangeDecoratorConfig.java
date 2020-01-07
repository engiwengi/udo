package net.urod.world.gen.decorator;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.decorator.DecoratorConfig;

public class ChanceRangeDecoratorConfig implements DecoratorConfig {
    public final int count;
    public final int bottomOffset;
    public final int topOffset;
    public final int maximum;
    public final int chance;

    public ChanceRangeDecoratorConfig(int count, int bottomOffset, int topOffset, int maximum, int chance) {
        this.count = count;
        this.bottomOffset = bottomOffset;
        this.topOffset = topOffset;
        this.maximum = maximum;
        this.chance = chance;
    }

    public static ChanceRangeDecoratorConfig deserialize(Dynamic<?> dynamic) {
        int i = dynamic.get("count").asInt(0);
        int j = dynamic.get("bottom_offset").asInt(0);
        int k = dynamic.get("top_offset").asInt(0);
        int l = dynamic.get("maximum").asInt(0);
        int m = dynamic.get("chance").asInt(0);
        return new ChanceRangeDecoratorConfig(i, j, k, l, m);
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("count"), ops.createInt(count), ops.createString("bottom_offset"), ops.createInt(bottomOffset), ops.createString("top_offset"),
                ops.createInt(topOffset), ops.createString("maximum"), ops.createInt(maximum))));
    }
}