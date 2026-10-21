package meow.binary.relicsofrain.item.relic;

import it.hurts.sskirillss.relics.api.relics.RelicTemplate;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.LevelingTemplate;
import meow.binary.relicsofrain.item.AbstractRORItem;
import meow.binary.relicsofrain.registry.RarityRegistry;

public class MimicCoreItem extends AbstractRORItem {
    public MimicCoreItem(Properties props) {
        super((new Properties()).rarity(RarityRegistry.UNCOMMON_RARITY.getValue()).stacksTo(1));
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
}
