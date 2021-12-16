package ca.lukegrahamlandry.findmyfriends.client;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.client.render.NamePlateRender;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import ca.lukegrahamlandry.findmyfriends.init.EntityInit;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    public static final KeyBinding OPEN_GUI = new KeyBinding("key.findmyfriends.gui", GLFW.GLFW_KEY_M, "key.categories.findmyfriends");

    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.NAME_PLATE.get(), NamePlateRender::new);

        ClientRegistry.registerKeyBinding(OPEN_GUI);
    }
}