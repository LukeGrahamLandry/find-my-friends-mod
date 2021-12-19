package ca.lukegrahamlandry.findmyfriends.events;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.ServerFindConfig;
import ca.lukegrahamlandry.findmyfriends.init.NetworkInit;
import ca.lukegrahamlandry.findmyfriends.network.ClearNamePacket;
import ca.lukegrahamlandry.findmyfriends.network.RenderNamePacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Random;

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
            if (ServerFindConfig.hideSneakingPlayers.get() && player.isShiftKeyDown()) continue; // skip sneaking people

            packets.add(new RenderNamePacket((ServerPlayerEntity) player));
        }
        // packets.add(new RenderNamePacket(pos[rand.nextInt(pos.length)], 65, pos[rand.nextInt(pos.length)], UUID.fromString("25399d17-a7e5-4bd3-a70c-39c6ff11b4df"), new StringTextComponent("test name here")));

        for (PlayerEntity player : event.world.players()){
            for (RenderNamePacket packet : packets){
                if (packet.uuid != player.getUUID()){
                    if (ServerFindConfig.maxDistance.get() >= 0){
                        // don't show far players based on config
                        double dist = player.distanceToSqr(packet.x, packet.y, packet.z);
                        if (dist > Math.pow(ServerFindConfig.maxDistance.get(), 2)){
                            continue;
                        }
                    }

                    NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), packet);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event){
        if (event.getPlayer().level.isClientSide()) return;

        ClearNamePacket packet = new ClearNamePacket((ServerPlayerEntity) event.getPlayer());
        for (PlayerEntity player : event.getPlayer().level.players()){
            NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), packet);
        }
    }

    @SubscribeEvent
    public static void onDimension(PlayerEvent.PlayerChangedDimensionEvent event){
        if (event.getPlayer().level.isClientSide()) return;

        ClearNamePacket packet = new ClearNamePacket((ServerPlayerEntity) event.getPlayer());

        World fromWorld = ((ServerWorld)event.getPlayer().level).getServer().getLevel(event.getFrom());
        if (fromWorld == null) return;

        for (PlayerEntity player : fromWorld.players()){
            NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), packet);
        }
    }
}
