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
import meow.binary.relicsofrain.api.IProcCoefficient;
import meow.binary.relicsofrain.api.ItemDamageSource;
import meow.binary.relicsofrain.api.effect.OnHitEffect;
import meow.binary.relicsofrain.entity.projectile.LightningArc;
import meow.binary.relicsofrain.item.AbstractRORItem;
import meow.binary.relicsofrain.registry.EntityRegistry;
import meow.binary.relicsofrain.registry.ItemRegistry;
import meow.binary.relicsofrain.registry.RarityRegistry;
import meow.binary.relicsofrain.util.RelicHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class UkuleleItem extends AbstractRORItem implements OnHitEffect, IProcCoefficient {
    public static final int PROC_COOLDOWN = 40;

    public UkuleleItem(Properties props) {
        super((new Item.Properties()).rarity(RarityRegistry.UNCOMMON_RARITY.getValue()).stacksTo(1));
    }

    @Override
    public RelicTemplate constructDefaultRelicTemplate() {
        return RelicTemplate.builder()
                .abilities(AbilitiesTemplate.builder()
                        .ability(AbilityTemplate.builder("lightning_chain")
                                .stat(AbilityStatTemplate.builder("proc_chance")
                                        .initialValue(0.15d, 0.18d)
                                        .targetValue(RelicsScalingModels.MULTIPLICATIVE_BASE.get(), 0.495)
                                        .thresholdValue(0, 1)
                                        .formatValue(value -> (int) Math.round(value * 100))
                                        .build())
                                .stat(AbilityStatTemplate.builder("max_targets")
                                        .initialValue(2, 4)
                                        .targetValue(RelicsScalingModels.ADDITIVE.get(), 19)
                                        .formatValue(Math::round)
                                        .build())
                                .stat(AbilityStatTemplate.builder("max_distance")
                                        .initialValue(4, 7)
                                        .targetValue(RelicsScalingModels.ADDITIVE.get(), 13.5)
                                        .formatValue(Math::round)
                                        .build())
                                .stat(AbilityStatTemplate.builder("damage_percentage")
                                        .initialValue(0.25d, 0.5d)
                                        .targetValue(RelicsScalingModels.ADDITIVE.get(), 1.0)
                                        .thresholdValue(0, 1)
                                        .formatValue(value -> (int) Math.round(value * 100))
                                        .build())
                                .experienceSources(ExperienceSourcesTemplate.builder()
                                        .source(ExperienceSourceTemplate.builder("lightning_chain").build())
                                        .build())
                                .build())
                        .build())
                .leveling(LevelingTemplate.builder()
                        .initialCost(100)
                        .step(175)
                        .maxRank(0)
                        .build())
                .build();
    }

    @Override
    public float getProcCoefficient() {
        return 0.2f;
    }

    @Override
    public int onHit(LivingDamageEvent.Post e) {
        int exitValue = OnHitEffect.super.onHit(e);
        if (exitValue != 1) return exitValue;

        Entity causingEntity = e.getSource().getEntity();
        Entity source = e.getSource().getDirectEntity();
        LivingEntity target = e.getEntity();
        if (EntityUtils.isAlliedTo(causingEntity, target)) return -1;
        if (e.getSource() instanceof ItemDamageSource proc && proc.getItemUsed().getItem() instanceof UkuleleItem) return -1;

        ItemStack stack = EntityUtils.findEquippedCurio(causingEntity, ItemRegistry.UKULELE.get());
        if (!(stack.getItem() instanceof IRelicItem relic)) return -1;
        LivingEntity bearer = causingEntity instanceof LivingEntity living ? living : null;
        if (target.level().random.nextFloat() > RelicHelper.stat(bearer, stack, "lightning_chain", "proc_chance")) return 0;

        LightningArc arc = new LightningArc(EntityRegistry.LIGHTNING_ARC.get(), target.level());
        arc.setOwner(causingEntity);
        arc.setDamage(RelicHelper.stat(bearer, stack, "lightning_chain", "damage_percentage") * e.getOriginalDamage());
        arc.setMaxDistance(RelicHelper.stat(bearer, stack, "lightning_chain", "max_distance"));
        arc.setTarget(target);
        arc.getBouncedTargets().add(target.getStringUUID());
        arc.setTargetsLeft((int) RelicHelper.stat(bearer, stack, "lightning_chain", "max_targets"));
        arc.setPos(target.getEyePosition());
        arc.setItemStack(stack);

        target.level().addFreshEntity(arc);

        return 1;
    }
}
