package net.urod.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.urod.UltraRichOreDeposits;

public class URODConfig {
    private boolean easySampler;

    static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(String.format("config.%s.title", UltraRichOreDeposits.MOD_ID));
        return builder.build();
    }

    public boolean isEasySampler() {
        return easySampler;
    }
}
