package net.urod.world.gen.decorator;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.urod.UltraRichOreDeposits;

public class ModDecorators {
    public static final Decorator<ChanceRangeDecoratorConfig> CHANCE_RANGE_COUNT = ModDecorators.register("chance_range_count", new ChanceCountRangeDecorator(ChanceRangeDecoratorConfig::deserialize));

    public ModDecorators() {
        UltraRichOreDeposits.getLogger().info(String.format("Registering %s Decorators", UltraRichOreDeposits.MOD_ID));
    }

    private static <T extends DecoratorConfig, G extends Decorator<T>> G register(String id, G decorator) {
        return Registry.register(Registry.DECORATOR, new Identifier(UltraRichOreDeposits.MOD_ID, id), decorator);
    }
}
