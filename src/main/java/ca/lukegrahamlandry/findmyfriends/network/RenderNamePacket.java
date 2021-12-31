package ca.lukegrahamlandry.findmyfriends.network;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.ServerFindConfig;
import ca.lukegrahamlandry.findmyfriends.entity.NamePlateEntity;
import ca.lukegrahamlandry.findmyfriends.init.EntityInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

// server -> client
public class RenderNamePacket {
    public final double x;
    public final double y;
    public final double z;
    public final UUID uuid;
    public final ITextComponent name;
    public final boolean showDist;
    public final int timeout;

    public RenderNamePacket(double x, double y, double z, UUID uuid, ITextComponent name, boolean showDist, int timeout) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.uuid = uuid;
        this.name = name;
        this.showDist = showDist;
        this.timeout = timeout;
    }

    public RenderNamePacket(ServerPlayerEntity player) {
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        this.uuid = player.getUUID();
        this.name = player.getName();
        this.showDist = ServerFindConfig.showDistance.get();
        this.timeout = ServerFindConfig.getUpdateInterval();
    }

    public RenderNamePacket(PacketBuffer buf) {
        this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readUUID(), buf.readComponent(), buf.readBoolean(), buf.readInt());
    }

    public static void toBytes(RenderNamePacket msg, PacketBuffer buf) {
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
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null || player.level == null) return;
        ClientWorld world = (ClientWorld) player.level;

        if (msg.uuid.equals(player.getUUID())) return; // dont show own name

        NamePlateEntity oldNamePlate = ModMain.namePlates.get(msg.uuid);
        if (oldNamePlate != null) oldNamePlate.remove();

        // making a new one every time so that if you teleported it will be added properly. not sure this is nessisary. consider memoizing with ModMain.namePlates
        NamePlateEntity namePlate = new NamePlateEntity(EntityInit.NAME_PLATE.get(), world);
        namePlate.setCustomName(msg.name);
        namePlate.targetUUID = msg.uuid;
        namePlate.showDist = msg.showDist;
        ModMain.namePlates.put(msg.uuid, namePlate);

        // move it to the right location
        namePlate.setCustomName(msg.name);
        namePlate.targetPosition = new Vector3d(msg.x, msg.y, msg.z);
        namePlate.updateLocation();

        world.putNonPlayerEntity(namePlate.getId(), namePlate);
    }
}

