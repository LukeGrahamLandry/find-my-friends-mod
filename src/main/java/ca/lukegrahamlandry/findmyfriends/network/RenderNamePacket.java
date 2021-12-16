package ca.lukegrahamlandry.findmyfriends.network;

import ca.lukegrahamlandry.findmyfriends.ClientFindConfig;
import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.entity.NamePlateEntity;
import ca.lukegrahamlandry.findmyfriends.init.EntityInit;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

// server -> client
public class RenderNamePacket {
    private final double x;
    private final double y;
    private final double z;
    public final UUID uuid;
    private final ITextComponent name;

    public RenderNamePacket(double x, double y, double z, UUID uuid, ITextComponent name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.uuid = uuid;
        this.name = name;
    }

    public RenderNamePacket(ServerPlayerEntity player) {
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        this.uuid = player.getUUID();
        this.name = player.getName();
    }

    public RenderNamePacket(PacketBuffer buf) {
        this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readUUID(), buf.readComponent());
    }

    public static void toBytes(RenderNamePacket msg, PacketBuffer buf) {
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
        buf.writeUUID(msg.uuid);
        buf.writeComponent(msg.name);
    }

    public static void handle(RenderNamePacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player == null || player.level == null) return;
            World world = player.level;

            NamePlateEntity namePlate = ModMain.namePlates.getOrDefault(msg.uuid, makeName(msg, world));

            // move it to the right location
            Vector3d targetPlayer = new Vector3d(msg.x, msg.y, msg.z);
            Vector3d direction = targetPlayer.subtract(player.getEyePosition(0));
            direction = direction.normalize().scale(ClientFindConfig.getFakeNameDisplayDistance());
            Vector3d position = player.position().add(direction);
            namePlate.setPos(position.x, position.y, position.z);

        });
        context.get().setPacketHandled(true);
    }

    private static NamePlateEntity makeName(RenderNamePacket msg, World world) {
        NamePlateEntity namePlate = new NamePlateEntity(EntityInit.NAME_PLATE.get(), world);
        namePlate.setCustomName(msg.name);
        ModMain.namePlates.put(msg.uuid, namePlate);
        world.addFreshEntity(namePlate);
        return namePlate;
    }
}

