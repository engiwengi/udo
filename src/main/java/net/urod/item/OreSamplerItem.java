package net.urod.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.urod.block.UltraRichOreBlock;

public class OreSamplerItem extends Item {
    private long lastUse;
    private long lastWarning;

    public OreSamplerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() == null) {
            return ActionResult.PASS;
        }
        World world = context.getWorld();
        if (!world.isClient) {
            BlockState state = world.getBlockState(context.getBlockPos());
            if (state.getBlock() instanceof UltraRichOreBlock) {
                String quality = new TranslatableText(String.format("property.urod.quality.%s", state.get(UltraRichOreBlock.QUALITY).asString())).asFormattedString();
                TranslatableText message = new TranslatableText("chat.urod.quality.inspect", quality);
                context.getPlayer().addChatMessage(message, true);
            }
        }
        return ActionResult.SUCCESS;
    }
}
