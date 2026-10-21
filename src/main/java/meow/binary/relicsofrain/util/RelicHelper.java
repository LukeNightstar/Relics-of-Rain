package meow.binary.relicsofrain.util;

import it.hurts.sskirillss.relics.api.relics.IRelicItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class RelicHelper {
    private RelicHelper() {
    }

    public static double stat(@Nullable LivingEntity entity, ItemStack stack, String ability, String stat) {
        return ((IRelicItem) stack.getItem()).getRelicData(entity, stack)
                .getAbilitiesData().getAbilityData(ability)
                .getStatData(stat).getValue();
    }

    public static void addExp(@Nullable LivingEntity entity, ItemStack stack, String ability, String source, int amount) {
        ((IRelicItem) stack.getItem()).getRelicData(entity, stack)
                .getLevelingData().addExperience(ability, source, amount);
    }
}
