package meow.binary.relicsofrain.item.relic;

import it.hurts.sskirillss.relics.api.relics.IRelicItem;
import it.hurts.sskirillss.relics.api.relics.RelicTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.AbilitiesTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.AbilityTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.ExperienceSourceTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.ExperienceSourcesTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.stats.AbilityStatTemplate;
import it.hurts.sskirillss.relics.init.RelicsScalingModels;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.LevelingTemplate;
import it.hurts.sskirillss.relics.utils.EntityUtils;
import meow.binary.relicsofrain.item.AbstractRORItem;
import meow.binary.relicsofrain.registry.ItemRegistry;
import meow.binary.relicsofrain.registry.RarityRegistry;
import meow.binary.relicsofrain.util.RelicHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class PlatinumHorseshoeItem extends AbstractRORItem {
    public PlatinumHorseshoeItem(Properties props) {
        super((new Properties()).rarity(RarityRegistry.LEGENDARY_RARITY.getValue()).stacksTo(1));
    }

    @Override
    public RelicTemplate constructDefaultRelicTemplate() {
        return RelicTemplate.builder()
                .abilities(AbilitiesTemplate.builder()
                        .ability(AbilityTemplate.builder("try_again")
                                .initialMaxLevel(5)
                                .stat(AbilityStatTemplate.builder("extra_rolls")
                                        .initialValue(1, 1)
                                        .formatValue(Math::round)
                                        .targetValue(RelicsScalingModels.ADDITIVE.get(), 6)
                                        .build())
                                .experienceSources(ExperienceSourcesTemplate.builder()
                                        .source(ExperienceSourceTemplate.builder("try_again").build())
                                        .build())
                                .build())
                        .build())
                .leveling(LevelingTemplate.builder()
                        .initialCost(100)
                        .step(100)
                        .maxRank(0)
                        .build())
                .build();
    }

    public static ItemStack findStack(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return ItemStack.EMPTY;
        return EntityUtils.findEquippedCurio(livingEntity, ItemRegistry.PLATINUM_HORSESHOE.get());
    }

    public static int getRolls(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return 1;
        ItemStack stack = PlatinumHorseshoeItem.findStack(livingEntity);
        if (!(stack.getItem() instanceof IRelicItem relic)) return 1;
        return (int) RelicHelper.stat(livingEntity, stack, "try_again", "extra_rolls") + 1;
    }

    public static void addExperience(Entity entity, int exp) {
        if (!(entity instanceof LivingEntity livingEntity)) return;
        ItemStack stack = PlatinumHorseshoeItem.findStack(livingEntity);
        if (!(stack.getItem() instanceof IRelicItem relic)) return;
        RelicHelper.addExp(livingEntity, stack, "try_again", "try_again", exp);
    }
}
