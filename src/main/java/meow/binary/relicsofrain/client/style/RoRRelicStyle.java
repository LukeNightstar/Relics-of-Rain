package meow.binary.relicsofrain.client.style;

import it.hurts.octostudios.octolib.util.OctoColor;
import it.hurts.sskirillss.relics.client.style.base.RelicStyle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Colours carried over from a relic's Relics 0.10 style data: the flawless shimmer (the old
 * beam colours) and, where the relic set them, the tooltip border colours.
 */
public class RoRRelicStyle extends RelicStyle {
    private final OctoColor flawlessStart;
    private final OctoColor flawlessEnd;
    @Nullable
    private final OctoColor borderTop;
    @Nullable
    private final OctoColor borderBottom;

    public RoRRelicStyle(int flawlessStart, int flawlessEnd) {
        this.flawlessStart = new OctoColor(flawlessStart);
        this.flawlessEnd = new OctoColor(flawlessEnd);
        this.borderTop = null;
        this.borderBottom = null;
    }

    public RoRRelicStyle(int flawlessStart, int flawlessEnd, int borderTop, int borderBottom) {
        this.flawlessStart = new OctoColor(flawlessStart);
        this.flawlessEnd = new OctoColor(flawlessEnd);
        this.borderTop = new OctoColor(borderTop);
        this.borderBottom = new OctoColor(borderBottom);
    }

    @Override
    public @Nullable OctoColor getFlawlessStartColor(LivingEntity entity, ItemStack stack) {
        return flawlessStart;
    }

    @Override
    public @Nullable OctoColor getFlawlessEndColor(LivingEntity entity, ItemStack stack) {
        return flawlessEnd;
    }

    @Override
    public @Nullable OctoColor getTopTooltipBorderColor(LivingEntity entity, ItemStack stack) {
        return borderTop;
    }

    @Override
    public @Nullable OctoColor getBottomTooltipBorderColor(LivingEntity entity, ItemStack stack) {
        return borderBottom;
    }

    // This mod ships no tooltip frame textures.
    @Override
    public @Nullable ResourceLocation getTooltipFrameTexture(LivingEntity entity, ItemStack stack) {
        return null;
    }

    @Override
    public @Nullable ResourceLocation getTooltipStarTexture(LivingEntity entity, ItemStack stack) {
        return null;
    }
}
