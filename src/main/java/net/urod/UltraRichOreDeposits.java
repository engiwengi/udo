package net.urod;

import net.fabricmc.api.ModInitializer;
import net.urod.block.ModBlocks;
import net.urod.block.entity.ModBlockEntities;
import net.urod.config.URODConfigManager;
import net.urod.item.ModItemGroups;
import net.urod.item.ModItems;
import net.urod.world.gen.ModGeneration;
import net.urod.world.gen.decorator.ModDecorators;
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
//       Block Related
        new ModBlocks();
        new ModBlockEntities();

//        Item Related
        new ModItemGroups();
        new ModItems();

//        World Gen Related
        new ModDecorators();
        new ModGeneration();

//        Config Related
        URODConfigManager.init();
    }
}
