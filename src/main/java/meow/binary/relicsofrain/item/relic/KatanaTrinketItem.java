package meow.binary.relicsofrain.item.relic;

import it.hurts.sskirillss.relics.api.relics.RelicTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.AbilitiesTemplate;
import it.hurts.sskirillss.relics.api.relics.abilities.AbilityTemplate;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.LevelingTemplate;
import meow.binary.relicsofrain.item.AbstractRORItem;
import meow.binary.relicsofrain.registry.RarityRegistry;

public class KatanaTrinketItem extends AbstractRORItem {
    public KatanaTrinketItem(Properties props) {
        super((new Properties()).rarity(RarityRegistry.LEGENDARY_RARITY.getValue()).stacksTo(1));
    }

    @Override
    public RelicTemplate constructDefaultRelicTemplate() {
        return RelicTemplate.builder()
                .abilities(AbilitiesTemplate.builder()
                        .ability(AbilityTemplate.builder("agility").initialMaxLevel(0).build())
                        .build())
                .leveling(LevelingTemplate.builder()
                        .initialCost(0)
                        .step(0)
                        .maxRank(0)
                        .build())
                .build();
    }


}
