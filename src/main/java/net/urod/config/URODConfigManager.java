package net.urod.config;

public class URODConfigManager {
    private static URODConfig config;

    public static URODConfig getConfig() {
        return config == null ? init() : config;
    }

    public static URODConfig init() {
        config = new URODConfig();
        return config;
    }
}
