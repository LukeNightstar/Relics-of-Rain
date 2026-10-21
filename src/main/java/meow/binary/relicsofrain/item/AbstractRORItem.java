package meow.binary.relicsofrain.item;

import it.hurts.sskirillss.relics.items.relics.base.WearableRelicItem;
import meow.binary.relicsofrain.RelicsOfRain;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractRORItem extends WearableRelicItem {
    public AbstractRORItem(Item.Properties properties) {
        super(properties);
    }

    public AbstractRORItem() {
        super((new Item.Properties()).rarity(Rarity.RARE).stacksTo(1));
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        entity.setGlowingTag(true);
        return super.onEntityItemUpdate(stack, entity);
    }

    @Override
    public String getConfigRoute() {
        return RelicsOfRain.MODID;
    }

    // The default points at a Relics documentation page that does not exist for this mod.
    @Override
    public @Nullable String getURI(LivingEntity entity, ItemStack stack) {
        return null;
    }
}
