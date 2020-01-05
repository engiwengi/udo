package net.urod;

import net.fabricmc.api.ModInitializer;
import net.urod.block.ModBlocks;
import net.urod.item.ModItemGroups;
import net.urod.item.ModItems;
import net.urod.world.gen.ModGeneration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UltraRichOreDeposits implements ModInitializer {
    public static final String MOD_ID = "urod";
    private static final Logger LOGGER = LogManager.getFormatterLogger(UltraRichOreDeposits.MOD_ID);

    public static Logger getLogger() {
        return UltraRichOreDeposits.LOGGER;
    }

    @Override
    public void onInitialize() {
        new ModBlocks();
        new ModItemGroups();
        new ModItems();
        new ModGeneration();
    }
}
