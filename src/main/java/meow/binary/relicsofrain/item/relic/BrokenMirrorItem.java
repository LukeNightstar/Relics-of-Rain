package meow.binary.relicsofrain.item.relic;

import it.hurts.sskirillss.relics.init.RelicsMobEffects;
import it.hurts.sskirillss.relics.api.relics.RelicTemplate;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.LevelingTemplate;
import meow.binary.relicsofrain.api.ItemDamageSource;
import meow.binary.relicsofrain.item.AbstractRORItem;
import meow.binary.relicsofrain.registry.ItemRegistry;
import meow.binary.relicsofrain.registry.KeyMappingRegistry;
import meow.binary.relicsofrain.registry.RarityRegistry;
import meow.binary.relicsofrain.util.DungeonFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.InputEvent;
import top.theillusivec4.curios.api.SlotContext;

public class BrokenMirrorItem extends AbstractRORItem {
    public BrokenMirrorItem(Properties props) {
        super((new Properties()).rarity(RarityRegistry.LUNAR_RARITY.getValue()).stacksTo(1));
    }

    @Override
    public RelicTemplate constructDefaultRelicTemplate() {
        return RelicTemplate.builder()
                .leveling(LevelingTemplate.builder()
                        .initialCost(0)
                        .step(0)
                        .maxRank(0)
                        .build())
                .build();
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (!playerIn.getCooldowns().isOnCooldown(ItemRegistry.BROKEN_MIRROR.asItem()) && !worldIn.isClientSide()) {
            ServerPlayer serverPlayer = (ServerPlayer) playerIn;
            ServerLevel serverLevel = (ServerLevel) serverPlayer.level();

            playerIn.startUsingItem(handIn);
            return InteractionResultHolder.pass(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (!world.isClientSide() && entity instanceof ServerPlayer player) {
            ServerLevel serverLevel = (ServerLevel) player.level();
            if (!player.isCreative()) player.getCooldowns().addCooldown(stack.getItem(), 1200);
            player.hurt(ItemDamageSource.get(DamageTypes.MAGIC, serverLevel, null, null, stack), 3);
            player.addEffect(new MobEffectInstance(RelicsMobEffects.BLEEDING, 110, 1, false, true, true));
            BlockPos blockPos = DungeonFinder.findNearestDungeon(serverLevel, player.blockPosition(), 10);
            if (blockPos == null) return stack;
            Vec3 pos = blockPos.getBottomCenter().add(1,0,0);
            player.teleportTo(pos.x, pos.y, pos.z);
            player.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 36000, 1, true, false, false));
        }
        return stack;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    public int getUseDuration(ItemStack pStack, LivingEntity entity) {
        return 40;
    }

    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    @EventBusSubscriber
    public static class ServerEvents {
        @SubscribeEvent
        public static void input(InputEvent.Key e) {
            if (FMLLoader.isProduction()) return;

            if (KeyMappingRegistry.FREEZE.consumeClick() && Minecraft.getInstance().player != null) {
                ClientPacketListener connection = Minecraft.getInstance().getConnection();
                if (connection == null) return;
                connection.sendCommand("tick "+(Minecraft.getInstance().player.level().tickRateManager().isFrozen() ? "unfreeze" : "freeze"));
            }
        }

        // TODO(lukenightstar): Broken Mirror obtain path. Relics 0.12 removed the Magic Mirror, so the
        // handler that turned a Magic Mirror dropped from 12+ blocks into a Broken Mirror is gone.
    }
}
