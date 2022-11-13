package ca.lukegrahamlandry.findmyfriends.events;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.client.ClientSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyboardEvents {
    public static boolean isActive = true;

    @SubscribeEvent
    public static void onPress(InputEvent.Key event){
        if (Minecraft.getInstance().player == null) return;

        if (ClientSetup.OPEN_GUI.consumeClick()) {
            isActive = !isActive;
        }
    }

    @SubscribeEvent
    public static void doname(RenderNameTagEvent event){
        if (event.getEntity() instanceof Player && Minecraft.getInstance().player != null && !Minecraft.getInstance().player.getUUID().equals(event.getEntity().getUUID()) && isActive) event.setResult(Event.Result.DENY);
    }
}