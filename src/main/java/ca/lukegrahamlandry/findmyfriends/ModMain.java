package ca.lukegrahamlandry.findmyfriends;

import ca.lukegrahamlandry.findmyfriends.entity.NamePlateEntity;
import ca.lukegrahamlandry.findmyfriends.init.NetworkInit;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ca.lukegrahamlandry.findmyfriends.init.EntityInit;

import java.util.HashMap;
import java.util.UUID;

@Mod(ModMain.MOD_ID)
public class ModMain {
    public static final String MOD_ID = "findmyfriends";

    public static HashMap<UUID, NamePlateEntity> namePlates = new HashMap<>();

    public ModMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EntityInit.ENTITY.register(modEventBus);
        NetworkInit.registerPackets();

        ServerFindConfig.init();
    }
}
