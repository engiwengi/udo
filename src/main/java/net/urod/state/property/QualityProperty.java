package net.urod.state.property;

import net.minecraft.state.property.EnumProperty;
import net.urod.util.Quality;

import java.util.Arrays;

public class QualityProperty extends EnumProperty<Quality> {
    private QualityProperty(String name) {
        super(name, Quality.class, Arrays.asList(Quality.values()));
    }

    public static QualityProperty of(String name) {
        return new QualityProperty(name);
    }

}
