package ca.lukegrahamlandry.findmyfriends.events;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.ServerFindConfig;
import ca.lukegrahamlandry.findmyfriends.init.NetworkInit;
import ca.lukegrahamlandry.findmyfriends.network.ClearNamePacket;
import ca.lukegrahamlandry.findmyfriends.network.RenderNamePacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MiscEventHandler {
    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event){
        if (event.phase == TickEvent.Phase.END || event.side == LogicalSide.CLIENT) return;

        if (event.world.getGameTime() % ServerFindConfig.getUpdateInterval() != 0) return;

        ArrayList<Object> packets = new ArrayList<>();
        for (Player player : event.world.players()){
            if (ServerFindConfig.hideSneakingPlayers.get() && player.isShiftKeyDown()) {
                packets.add(new ClearNamePacket((ServerPlayer) player));
            } else {
                packets.add(new RenderNamePacket((ServerPlayer) player));
            }
        }
        for (Player player : event.world.players()){
            for (Object packet : packets){
                if (ServerFindConfig.maxDistance.get() >= 0 && packet instanceof RenderNamePacket){
                    // don't show far players based on config
                    RenderNamePacket p = (RenderNamePacket) packet;
                    double dist = player.distanceToSqr(p.x, p.y, p.z);
                    if (dist > Math.pow(ServerFindConfig.maxDistance.get(), 2)){
                        continue;
                    }
                }

                NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
            }
        }

    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event){
        if (event.getPlayer().level.isClientSide()) return;

        ClearNamePacket packet = new ClearNamePacket((ServerPlayer) event.getPlayer());
        for (Player player : event.getPlayer().level.players()){
            NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
        }
    }

    @SubscribeEvent
    public static void onDimension(PlayerEvent.PlayerChangedDimensionEvent event){
        if (event.getPlayer().level.isClientSide()) return;

        ClearNamePacket packet = new ClearNamePacket((ServerPlayer) event.getPlayer());

        Level fromWorld = ((ServerLevel)event.getPlayer().level).getServer().getLevel(event.getFrom());
        if (fromWorld == null) return;

        for (Player player : fromWorld.players()){
            NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event){
        if (event.getEntity().level.isClientSide()) return;

        if (event.getEntity() instanceof Player){
            ClearNamePacket packet = new ClearNamePacket((ServerPlayer) event.getEntity());

            for (Player player : event.getEntity().level.players()){
                NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
            }
        }

    }
}
