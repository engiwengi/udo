package net.urod.config;

import java.util.Objects;

public class URODConfigManager {
    private static URODConfig config;

    public static URODConfig getConfig() {
        return Objects.requireNonNull(config);
    }
}
