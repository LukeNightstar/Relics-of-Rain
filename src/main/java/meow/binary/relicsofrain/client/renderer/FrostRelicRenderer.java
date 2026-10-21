package meow.binary.relicsofrain.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import meow.binary.relicsofrain.RelicsOfRain;
import meow.binary.relicsofrain.client.model.CurioPartsModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class FrostRelicRenderer implements ICurioRenderer {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(RelicsOfRain.MODID, "frost_relic"), "frost_relic");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(RelicsOfRain.MODID, "textures/item/model/frost_relic.png");

    public static LayerDefinition createLayer() {

        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0.4f), 0);
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 1).addBox(2.61F, 0.0F, -2.5F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        CurioPartsModel model = new CurioPartsModel(Minecraft.getInstance().getEntityModels().bakeLayer(LAYER), "left_leg");
        matrixStack.pushPose();
        LivingEntity entity = slotContext.entity();
        ICurioRenderer.followBodyRotations(entity, model);
        model.prepareMobModel(entity, limbSwing * 0.76f, limbSwingAmount / 4f, partialTicks);
        model.setupAnim(entity, limbSwing * 0.76f, limbSwingAmount / 4f, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(TEXTURE), stack.hasFoil());
        matrixStack.translate(0, -0.125, 0);
        model.renderToBuffer(matrixStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY);
        matrixStack.popPose();
    }
}
