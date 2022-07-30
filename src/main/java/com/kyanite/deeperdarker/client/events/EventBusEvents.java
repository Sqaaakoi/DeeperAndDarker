package com.kyanite.deeperdarker.client.events;

import com.kyanite.deeperdarker.DeeperAndDarker;
import com.kyanite.deeperdarker.client.rendering.armor.renderer.SculkArmorRenderer;
import com.kyanite.deeperdarker.registry.items.SculkArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = DeeperAndDarker.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EventBusEvents {
    @SubscribeEvent
    public static void armorRenderers(final EntityRenderersEvent.AddLayers e) {
        GeoArmorRenderer.registerArmorRenderer(SculkArmorItem.class, new SculkArmorRenderer());
    }
}