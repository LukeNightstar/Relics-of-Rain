package meow.binary.relicsofrain.client;

import it.hurts.sskirillss.relics.init.RelicsRelicStyles;
import meow.binary.relicsofrain.RelicsOfRain;
import meow.binary.relicsofrain.client.renderer.FrostRelicRenderer;
import meow.binary.relicsofrain.client.renderer.PlatinumHorseshoeRenderer;
import meow.binary.relicsofrain.client.renderer.UkuleleRenderer;
import meow.binary.relicsofrain.client.style.RoRRelicStyle;
import meow.binary.relicsofrain.registry.ItemRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@EventBusSubscriber(modid = RelicsOfRain.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    // The old default beam colours, which Relics used for the flawless shimmer.
    private static final int DEFAULT_FLAWLESS_START = 0xFFFFFF00;
    private static final int DEFAULT_FLAWLESS_END = 0x00FF0000;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RelicsRelicStyles.register(ItemRegistry.BUSTLING_FUNGUS.get(), () -> new RoRRelicStyle(0xFF58f99f, 0x00005346, 0xff88ee88, 0xff0a7a6c));
            RelicsRelicStyles.register(ItemRegistry.SOLDIER_SYRINGE.get(), () -> new RoRRelicStyle(0xFF0a70dd, 0x00002042));
            RelicsRelicStyles.register(ItemRegistry.ENERGY_DRINK.get(), () -> new RoRRelicStyle(-65281, 255, 0xffb277f8, 0xff633ee0));
            RelicsRelicStyles.register(ItemRegistry.CROWBAR.get(), () -> new RoRRelicStyle(0x90909090, 0x00ffffff));
            RelicsRelicStyles.register(ItemRegistry.UKULELE.get(), () -> new RoRRelicStyle(DEFAULT_FLAWLESS_START, DEFAULT_FLAWLESS_END, 0xff965740, 0xff4b1f16));
            RelicsRelicStyles.register(ItemRegistry.WAX_QUAIL.get(), () -> new RoRRelicStyle(DEFAULT_FLAWLESS_START, DEFAULT_FLAWLESS_END));
            RelicsRelicStyles.register(ItemRegistry.FROST_RELIC.get(), () -> new RoRRelicStyle(0x770971cf, 0x0083f1f0, 0xff83f1f0, 0xff0971cf));
            RelicsRelicStyles.register(ItemRegistry.KATANA_TRINKET.get(), () -> new RoRRelicStyle(0x0, 0x0));
            RelicsRelicStyles.register(ItemRegistry.PLATINUM_HORSESHOE.get(), () -> new RoRRelicStyle(DEFAULT_FLAWLESS_START, DEFAULT_FLAWLESS_END, 0xffbfc9bf, 0xff939c94));
            RelicsRelicStyles.register(ItemRegistry.BROKEN_MIRROR.get(), () -> new RoRRelicStyle(0x0, 0x0, 0xff007582, 0xff70cbce));
            RelicsRelicStyles.register(ItemRegistry.UNLABELED_SYRINGE.get(), () -> new RoRRelicStyle(0x775027d8, 0x00e01062, 0xffaa95ef, 0xffd4d0ef));
            RelicsRelicStyles.register(ItemRegistry.IRRADIANT_PEARL.get(), () -> new RoRRelicStyle(0xff57d1e4, 0x0024ffaa));
            RelicsRelicStyles.init();
        });

        CuriosRendererRegistry.register(ItemRegistry.FROST_RELIC.get(), FrostRelicRenderer::new);
        CuriosRendererRegistry.register(ItemRegistry.PLATINUM_HORSESHOE.get(), PlatinumHorseshoeRenderer::new);
        CuriosRendererRegistry.register(ItemRegistry.UKULELE.get(), UkuleleRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FrostRelicRenderer.LAYER, FrostRelicRenderer::createLayer);
        event.registerLayerDefinition(PlatinumHorseshoeRenderer.LAYER, PlatinumHorseshoeRenderer::createLayer);
        event.registerLayerDefinition(UkuleleRenderer.LAYER, UkuleleRenderer::createLayer);
    }
}
