package ca.lukegrahamlandry.findmyfriends.events;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.client.ClientSetup;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyboardEvents {
    @SubscribeEvent
    public static void onPress(InputEvent.KeyInputEvent event){
        if (Minecraft.getInstance().player == null) return;

        if (ClientSetup.OPEN_GUI.isDown()) {
            // NetworkInit.INSTANCE.sendToServer(new MercKeybindPacket(0));
        }
    }
}