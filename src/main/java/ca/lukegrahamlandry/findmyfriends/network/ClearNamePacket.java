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
public class ClearNamePacket {
    public final UUID uuid;

    public ClearNamePacket(UUID uuid) {
        this.uuid = uuid;
    }

    public ClearNamePacket(ServerPlayerEntity player) {
        this.uuid = player.getUUID();
    }

    public ClearNamePacket(PacketBuffer buf) {
        this(buf.readUUID());
    }

    public static void toBytes(ClearNamePacket msg, PacketBuffer buf) {
        buf.writeUUID(msg.uuid);
    }

    public static void handle(ClearNamePacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            handlePacket(msg);
        });
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handlePacket(ClearNamePacket msg) {
        ModMain.namePlates.remove(msg.uuid);
    }
}

