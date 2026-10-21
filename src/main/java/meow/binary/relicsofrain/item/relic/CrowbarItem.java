package meow.binary.relicsofrain.item.relic;

import it.hurts.sskirillss.relics.api.relics.IRelicItem;
import it.hurts.sskirillss.relics.api.relics.RelicTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.AbilitiesTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.AbilityTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.ExperienceSourceTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.ExperienceSourcesTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.stats.AbilityStatTemplate;
import it.hurts.sskirillss.relics.init.RelicsCreativeTabs;
import it.hurts.sskirillss.relics.init.RelicsScalingModels;
import it.hurts.sskirillss.relics.items.misc.CreativeContentConstructor;
import it.hurts.sskirillss.relics.items.misc.ICreativeTabContent;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.LevelingTemplate;
import meow.binary.relicsofrain.RelicsOfRain;
import meow.binary.relicsofrain.api.ItemDamageSource;
import meow.binary.relicsofrain.util.RelicHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

@EventBusSubscriber
public class CrowbarItem extends SwordItem implements IRelicItem, ICreativeTabContent {
    public static final float ENTITY_HEALTH_THRESHOLD = 0.9f;

    public CrowbarItem(Properties props) {
        super(new SimpleTier(
                        Tiers.IRON.getIncorrectBlocksForDrops(),
                        640,
                        Tiers.IRON.getSpeed(),
                        Tiers.IRON.getAttackDamageBonus(),
                        Tiers.IRON.getEnchantmentValue(),
                        Tiers.IRON::getRepairIngredient
                ), new Properties()
                        .rarity(Rarity.COMMON)
                        .stacksTo(1)
                        .attributes(SwordItem.createAttributes(Tiers.IRON, 4, -2.6F))
        );
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem() || slotChanged;
    }

    @Override
    public RelicTemplate constructDefaultRelicTemplate() {
        return RelicTemplate.builder()
                .abilities(AbilitiesTemplate.builder()
                        .ability(AbilityTemplate.builder("heavy_hitter")
                                .initialMaxLevel(20)
                                .stat(AbilityStatTemplate.builder("bonus_damage_multiplier")
                                        .initialValue(0.5, 1.0)
                                        .formatValue(value -> Math.round(value * 100))
                                        .targetValue(RelicsScalingModels.ADDITIVE.get(), 6.0)
                                        .build())
                                .experienceSources(ExperienceSourcesTemplate.builder()
                                        .source(ExperienceSourceTemplate.builder("heavy_hitter").build())
                                        .build())
                                .build())
                        .ability(AbilityTemplate.builder("pry_it_open")
                                .initialMaxLevel(0)
                                .build())
                        .build())
                .leveling(LevelingTemplate.builder()
                        .maxRank(0)
                        .build())
                .build();
    }

    @SubscribeEvent
    public static void modifyDamage(LivingDamageEvent.Pre e) {
        Entity entity = e.getSource().getEntity();
        LivingEntity target = e.getEntity();
        ItemStack stack = entity != null
                ? entity.getWeaponItem() != null
                ? entity.getWeaponItem()
                : ItemStack.EMPTY
                : ItemStack.EMPTY;

        if (target.level().isClientSide
                || target.getHealth() / target.getMaxHealth() < ENTITY_HEALTH_THRESHOLD
                || e.getSource() instanceof ItemDamageSource
                || !(entity instanceof LivingEntity source)
                || !(stack.getItem() instanceof CrowbarItem relic)
        ) return;

        if (isBroken(stack)) {
            e.setNewDamage(1);
            return;
        }

        e.setNewDamage(e.getNewDamage() * (1f + (float) RelicHelper.stat(source, stack, "heavy_hitter", "bonus_damage_multiplier")));
        target.knockback(1.25, source.getX() - target.getX(), source.getZ() - target.getZ());
        target.level().playSound(null, target, SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL, 0.4f, 1.35f);
        if (source instanceof Player player && player.getAttackStrengthScale(0) >= 0.95f) {
            RelicHelper.addExp(source, stack, "heavy_hitter", "heavy_hitter", 1);
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        if (isBroken(stack)) {
            return 0;
        }
        int result = super.damageItem(stack, amount, entity, onBroken);
        return Math.min(result, 1);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable(
                "tooltip.relicsofrain.crowbar.damage_tip",
                Math.round(ENTITY_HEALTH_THRESHOLD * 100)
        ).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable(
                "tooltip.relicsofrain.crowbar.damage",
                Math.round(RelicHelper.stat(null, stack, "heavy_hitter", "bonus_damage_multiplier") * 100)
        ).withStyle(ChatFormatting.DARK_GREEN));
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        entity.setGlowingTag(true);
        return super.onEntityItemUpdate(stack, entity);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (CrowbarItem.isBroken(stack)) {
            return 0f;
        }
        if (state.is(BlockTags.PLANKS)) {
            return 100.0F;
        }
        return super.getDestroySpeed(stack, state);
    }


    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(BlockTags.PLANKS) || super.isCorrectToolForDrops(stack, state);
    }

    @Override
    public String getConfigRoute() {
        return RelicsOfRain.MODID;
    }

    @Override
    public void gatherCreativeTabContent(CreativeContentConstructor creativeContentConstructor) {
        creativeContentConstructor.entry(RelicsCreativeTabs.RELICS_TAB.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, this);
    }

    public static boolean isBroken(ItemStack stack) {
        return stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }
}
