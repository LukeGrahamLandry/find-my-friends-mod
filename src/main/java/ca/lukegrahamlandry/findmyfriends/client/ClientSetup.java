package ca.lukegrahamlandry.findmyfriends.client;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.client.render.NamePlateRender;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ca.lukegrahamlandry.findmyfriends.init.EntityInit;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    public static final KeyMapping OPEN_GUI = new KeyMapping("key.findmyfriends.gui", GLFW.GLFW_KEY_M, "key.categories.findmyfriends");

    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityInit.NAME_PLATE.get(), NamePlateRender::new);

        ClientRegistry.registerKeyBinding(OPEN_GUI);
    }
}