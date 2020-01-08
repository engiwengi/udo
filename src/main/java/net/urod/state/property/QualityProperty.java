package net.urod.state.property;

import net.minecraft.state.property.EnumProperty;
import net.urod.util.OreQuality;

import java.util.Arrays;

public class QualityProperty extends EnumProperty<OreQuality> {
    private QualityProperty(String name) {
        super(name, OreQuality.class, Arrays.asList(OreQuality.values()));
    }

    public static QualityProperty of(String name) {
        return new QualityProperty(name);
    }

}
