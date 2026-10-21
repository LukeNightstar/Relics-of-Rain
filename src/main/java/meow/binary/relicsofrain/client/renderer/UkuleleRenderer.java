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

public class UkuleleRenderer implements ICurioRenderer {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(RelicsOfRain.MODID, "ukulele"), "ukulele");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(RelicsOfRain.MODID, "textures/item/model/ukulele.png");

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0.4F), 0F);
        PartDefinition partdefinition = meshdefinition.getRoot();


        PartDefinition root = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition body = root.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -0.5F, 6.0F, 11.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-4.0F, 1.0F, 0.495F, 8.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 0).addBox(-1.0F, -11.0F, 1.5F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 3.505F, 0.0F, 0.0F, -0.3927F));
        body.addOrReplaceChild("cube_5_r1", CubeListBuilder.create().texOffs(20, 14).addBox(-0.995F, -2.85F, 0.0F, 1.99F, 2.85F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.6173F, 0.4239F, -0.3927F, 0.0F, 0.0F));
        body.addOrReplaceChild("cube_5_r2", CubeListBuilder.create().texOffs(20, 18).addBox(-2.5F, -3.5F, -0.5F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(18, 9).addBox(-1.5F, -4.0F, -1.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.0F, 2.5F, 0.3054F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);

    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        CurioPartsModel model = new CurioPartsModel(Minecraft.getInstance().getEntityModels().bakeLayer(LAYER), "body");

        LivingEntity entity = slotContext.entity();

        matrixStack.pushPose();

        ICurioRenderer.followBodyRotations(entity, model);

        //ICurioRenderer.translateIfSneaking(matrixStack, entity);
        //ICurioRenderer.rotateIfSneaking(matrixStack,entity);


        model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(TEXTURE), stack.hasFoil());
        model.renderToBuffer(matrixStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
    }
}
