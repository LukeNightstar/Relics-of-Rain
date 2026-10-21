package meow.binary.relicsofrain.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

import java.util.Arrays;
import java.util.List;

/**
 * A humanoid-shaped model that renders only the named root children, so a curio can attach
 * its geometry to a humanoid part (and follow that part's animation) without drawing the rest.
 */
public class CurioPartsModel extends HumanoidModel<LivingEntity> {
    private final List<ModelPart> parts;

    public CurioPartsModel(ModelPart root, String... parts) {
        super(root);
        this.parts = Arrays.stream(parts).map(root::getChild).toList();
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return parts;
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return List.of();
    }
}
