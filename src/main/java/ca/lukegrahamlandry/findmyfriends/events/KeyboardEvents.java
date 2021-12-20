package ca.lukegrahamlandry.findmyfriends.events;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.client.ClientSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyboardEvents {
    public static boolean isActive = true;

    @SubscribeEvent
    public static void onPress(InputEvent.KeyInputEvent event){
        if (Minecraft.getInstance().player == null) return;

        if (ClientSetup.OPEN_GUI.consumeClick()) {
            isActive = !isActive;
            // NetworkInit.INSTANCE.sendToServer(new tooglepacket());
        }
    }

    @SubscribeEvent
    public static void doname(RenderNameplateEvent event){
        if (event.getEntity() instanceof PlayerEntity && Minecraft.getInstance().player != null) event.setResult(Event.Result.DENY);
    }
}