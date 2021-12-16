package ca.lukegrahamlandry.findmyfriends.events;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.ServerFindConfig;
import ca.lukegrahamlandry.findmyfriends.init.NetworkInit;
import ca.lukegrahamlandry.findmyfriends.network.RenderNamePacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MiscEventHandler {
    static final double[] pos = new double[]{0};
    static final Random rand = new Random();

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event){
        if (event.phase == TickEvent.Phase.END || event.side == LogicalSide.CLIENT) return;

        if (event.world.getGameTime() % ServerFindConfig.getUpdateInterval() != 0) return;

        ArrayList<RenderNamePacket> packets = new ArrayList<>();
        for (PlayerEntity player : event.world.players()){
            packets.add(new RenderNamePacket((ServerPlayerEntity) player));
        }
        packets.add(new RenderNamePacket(pos[rand.nextInt(pos.length)], 65, pos[rand.nextInt(pos.length)], UUID.fromString("25399d17-a7e5-4bd3-a70c-39c6ff11b4df"), new StringTextComponent("test name here")));

        for (PlayerEntity player : event.world.players()){
            for (RenderNamePacket packet : packets){
                if (packet.uuid != player.getUUID()){
                    NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), packet);
                }
            }
        }
    }
}
