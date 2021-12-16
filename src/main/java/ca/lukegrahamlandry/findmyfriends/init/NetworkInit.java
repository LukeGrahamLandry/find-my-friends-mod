package ca.lukegrahamlandry.findmyfriends.init;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.network.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkInit {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerPackets(){
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ModMain.MOD_ID, "findmyfriends"), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(), RenderNamePacket.class, RenderNamePacket::toBytes, RenderNamePacket::new, RenderNamePacket::handle);
    }
}
