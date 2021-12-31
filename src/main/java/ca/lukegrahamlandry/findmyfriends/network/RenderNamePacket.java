package ca.lukegrahamlandry.findmyfriends.network;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.ServerFindConfig;
import ca.lukegrahamlandry.findmyfriends.entity.NamePlateEntity;
import ca.lukegrahamlandry.findmyfriends.init.EntityInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

// server -> client
public class RenderNamePacket {
    public final double x;
    public final double y;
    public final double z;
    public final UUID uuid;
    public final Component name;
    public final boolean showDist;
    public final int timeout;

    public RenderNamePacket(double x, double y, double z, UUID uuid, Component name, boolean showDist, int timeout) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.uuid = uuid;
        this.name = name;
        this.showDist = showDist;
        this.timeout = timeout;
    }

    public RenderNamePacket(ServerPlayer player) {
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        this.uuid = player.getUUID();
        this.name = player.getName();
        this.showDist = ServerFindConfig.showDistance.get();
        this.timeout = ServerFindConfig.getUpdateInterval();
    }

    public RenderNamePacket(FriendlyByteBuf buf) {
        this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readUUID(), buf.readComponent(), buf.readBoolean(), buf.readInt());
    }

    public static void toBytes(RenderNamePacket msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
        buf.writeUUID(msg.uuid);
        buf.writeComponent(msg.name);
        buf.writeBoolean(msg.showDist);
        buf.writeInt(msg.timeout);
    }

    public static void handle(RenderNamePacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            handlePacket(msg);
        });
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handlePacket(RenderNamePacket msg) {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.level == null) return;
        ClientLevel world = (ClientLevel) player.level;

        if (msg.uuid.equals(player.getUUID())) return; // dont show own name

        NamePlateEntity oldNamePlate = ModMain.namePlates.get(msg.uuid);
        if (oldNamePlate != null) oldNamePlate.remove(Entity.RemovalReason.DISCARDED);

        // making a new one every time so that if you teleported it will be added properly. not sure this is nessisary. consider memoizing with ModMain.namePlates
        NamePlateEntity namePlate = new NamePlateEntity(EntityInit.NAME_PLATE.get(), world);
        namePlate.setCustomName(msg.name);
        namePlate.targetUUID = msg.uuid;
        namePlate.showDist = msg.showDist;
        ModMain.namePlates.put(msg.uuid, namePlate);

        // move it to the right location
        namePlate.setCustomName(msg.name);
        namePlate.targetPosition = new Vec3(msg.x, msg.y, msg.z);
        namePlate.updateLocation();

        world.putNonPlayerEntity(namePlate.getId(), namePlate);
    }
}

