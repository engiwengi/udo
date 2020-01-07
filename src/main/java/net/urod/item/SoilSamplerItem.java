package net.urod.item;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.urod.block.entity.UltraRichOreBlockEntity;
import net.urod.config.URODConfigManager;

import java.util.Map;

public class SoilSamplerItem extends Item {
    private long lastUse;
    private long lastWarning;

    public SoilSamplerItem(Settings settings) {
        super(settings);
    }

    private static int getSquaredHorizontalDistance(Vec3i vec1, Vec3i vec2) {
        float f = (float) Math.abs(vec1.getX() - vec2.getX());
        float h = (float) Math.abs(vec1.getZ() - vec2.getZ());
        return (int) (f * f + h * h);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() == null) {
            return ActionResult.PASS;
        }
        World world = context.getWorld();
        if (!world.isClient) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUse < 1000) {
                if (currentTime - lastWarning > 500) {
                    lastWarning = currentTime;
                    context.getPlayer().addChatMessage(new LiteralText("Sampler still recharging..."), true);
                }
                return ActionResult.PASS;
            }
            lastUse = currentTime;
            Map<Block, Double> nearbyOres;
            nearbyOres = findNearbyOres(context.getBlockPos(), (ServerWorld) world);
            if (nearbyOres.isEmpty()) {
                context.getPlayer().addChatMessage(new LiteralText("No trace of ores"), true);
            } else {
                for (Map.Entry<Block, Double> entry : nearbyOres.entrySet()) {
                    Text message;
                    if (URODConfigManager.getConfig().isEasySampler()) {
                        message = new LiteralText(String.format("%s found %s blocks away", entry.getKey().getName().asFormattedString(), Math.sqrt(entry.getValue())));
                    } else {
                        message = new LiteralText(String.format("%s of %s found", remapDistanceToString(entry.getValue()), entry.getKey().getName().asFormattedString()));
                    }
                    context.getPlayer().addChatMessage(message, true);
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    private String remapDistanceToString(double i) {
        if (i < 16 * 16) {
            return "Large traces";
        } else if (i < 32 * 32) {
            return "Traces";
        } else if (i < 64 * 64) {
            return "Slight traces";
        } else {
            return "Very slight traces";
        }
    }

    private Map<Block, Double> findNearbyOres(BlockPos pos, ServerWorld world) {
        Map<Block, Double> map = Maps.newHashMap();
        for (BlockEntity blockEntity : world.blockEntities) {
            if (blockEntity instanceof UltraRichOreBlockEntity) {
                Block block = world.getBlockState(blockEntity.getPos()).getBlock();
                double distance = map.getOrDefault(block, Double.MAX_VALUE);
                double blockDistance = pos.getSquaredDistance(blockEntity.getPos());
                if (blockDistance < distance && blockDistance < 96 * 96 /* max radius 64 blocks */) {
                    map.put(block, blockDistance);
                }
            }
        }
        return map;
    }
}
