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
import it.hurts.sskirillss.relics.utils.MathUtils;
import meow.binary.relicsofrain.RelicsOfRain;
import meow.binary.relicsofrain.item.AbstractRORItem;
import meow.binary.relicsofrain.util.RelicHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

public class EnergyDrinkItem extends AbstractRORItem {
    public EnergyDrinkItem(Properties props) {
        super((new Properties()).rarity(Rarity.COMMON).stacksTo(1));
    }

    @Override
    public RelicTemplate constructDefaultRelicTemplate() {
        return RelicTemplate.builder()
                .abilities(AbilitiesTemplate.builder()
                        .ability(AbilityTemplate.builder("speed_boost")
                                .initialMaxLevel(20)
                                .stat(AbilityStatTemplate.builder("sprint_multiplier")
                                        .initialValue(0.1875d, 0.1918d) // 119.23% = 125% of sprinting speed
                                        .targetValue(RelicsScalingModels.ADDITIVE.get(), 2.1148)
                                        .formatValue(value -> (MathUtils.round(value * 100 * 1.3f, 1)) + 0.1) //sprinting speed calculation tomfoolery
                                        .build())
                                .experienceSources(ExperienceSourcesTemplate.builder()
                                        .source(ExperienceSourceTemplate.builder("speed_boost").build())
                                        .build())
                                .build())
                        .build())
                .leveling(LevelingTemplate.builder()
                        .initialCost(100)
                        .step(150)
                        .maxRank(0)
                        .build())
                .build();
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(stack.getItem() instanceof IRelicItem relicItem)) return;
        AttributeInstance attr = slotContext.entity().getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr == null) return;
        attr.addOrUpdateTransientModifier(
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(RelicsOfRain.MODID, "sprint_bonus"),
                        slotContext.entity().isSprinting() ? RelicHelper.stat(slotContext.entity(), stack, "speed_boost", "sprint_multiplier") : 0,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        if (slotContext.entity().level().isClientSide) return;
        if (slotContext.entity().isSprinting() && slotContext.entity().tickCount % 20 == 0)
            RelicHelper.addExp(slotContext.entity(), stack, "speed_boost", "speed_boost", 1);
    }
}
