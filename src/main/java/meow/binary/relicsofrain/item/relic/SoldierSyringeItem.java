package meow.binary.relicsofrain.item.relic;

import it.hurts.sskirillss.relics.api.relics.RelicTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.AbilitiesTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.AbilityTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.ExperienceSourceTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.ExperienceSourcesTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.stats.AbilityStatTemplate;
import it.hurts.sskirillss.relics.init.RelicsScalingModels;
import it.hurts.sskirillss.relics.items.relics.base.data.RelicAttributeModifier;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.LevelingTemplate;
import it.hurts.sskirillss.relics.utils.EntityUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import meow.binary.relicsofrain.item.AbstractRORItem;
import meow.binary.relicsofrain.registry.ItemRegistry;
import meow.binary.relicsofrain.util.RelicHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class SoldierSyringeItem extends AbstractRORItem {
    public SoldierSyringeItem(Properties props) {
        super((new Properties()).rarity(Rarity.COMMON).stacksTo(1));
    }


    @Override
    public RelicTemplate constructDefaultRelicTemplate() {
        return RelicTemplate.builder()
                .abilities(AbilitiesTemplate.builder()
                        .ability(AbilityTemplate.builder("attack_speed")
                                .initialMaxLevel(20)
                                .stat(AbilityStatTemplate.builder("attack_speed")
                                        .initialValue(0.075, 0.15)
                                        .targetValue(RelicsScalingModels.ADDITIVE.get(), 1.65)
                                        .formatValue(value -> MathUtils.round(value*100, 1))
                                        .build())
                                .experienceSources(ExperienceSourcesTemplate.builder()
                                        .source(ExperienceSourceTemplate.builder("attack_speed").build())
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
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (!((stack.getItem() instanceof SoldierSyringeItem))) return super.interactLivingEntity(stack, player, interactionTarget, usedHand);;
        if (!(interactionTarget instanceof Zombie zombie)) return super.interactLivingEntity(stack, player, interactionTarget, usedHand);;

        player.setItemInHand(usedHand, new ItemStack(ItemRegistry.UNLABELED_SYRINGE.get()));
        player.level().playSound(null, player.blockPosition(), SoundEvents.VEX_HURT, SoundSource.HOSTILE, 1f, 1f);
        zombie.invulnerableTime = 0;
        zombie.hurt(zombie.level().damageSources().playerAttack(player), 10f);

        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable RelicAttributeModifier getRelicAttributeModifiers(LivingEntity entity, ItemStack stack) {
        return RelicAttributeModifier.builder()
                .attribute(new RelicAttributeModifier.Modifier(
                        Attributes.ATTACK_SPEED,
                        (float) RelicHelper.stat(entity, stack, "attack_speed", "attack_speed"),
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ))
                .build();
    }

    @SubscribeEvent
    public static void damage(LivingIncomingDamageEvent e) {
        if (e.getEntity().level().isClientSide) return;
        if (!(e.getSource().getDirectEntity() instanceof LivingEntity livingEntity)) return;
        ItemStack stack = EntityUtils.findEquippedCurio(livingEntity, ItemRegistry.SOLDIER_SYRINGE.get());
        if (!((stack.getItem() instanceof SoldierSyringeItem relic))) return;
        if (e.getAmount() >= 1) RelicHelper.addExp(livingEntity, stack, "attack_speed", "attack_speed", 1);
    }

    @SubscribeEvent
    public static void convertSyringe(LivingIncomingDamageEvent e) {
        if (e.getEntity().level().isClientSide) return;
        if (!(e.getSource().getDirectEntity() instanceof Player player)) return;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!((stack.getItem() instanceof SoldierSyringeItem))) return;
        if (!(e.getEntity() instanceof Zombie zombie)) return;

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.UNLABELED_SYRINGE.get()));
        player.level().playSound(null, player.blockPosition(), SoundEvents.VEX_HURT, SoundSource.HOSTILE, 1f, 1f);
        zombie.invulnerableTime = 0;
        zombie.hurt(e.getSource(), 10f);
    }
}
