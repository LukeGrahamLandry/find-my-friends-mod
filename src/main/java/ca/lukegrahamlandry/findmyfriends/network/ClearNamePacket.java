package ca.lukegrahamlandry.findmyfriends.network;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

// server -> client
public class ClearNamePacket {
    public final UUID uuid;

    public ClearNamePacket(UUID uuid) {
        this.uuid = uuid;
    }

    public ClearNamePacket(ServerPlayer player) {
        this.uuid = player.getUUID();
    }

    public ClearNamePacket(FriendlyByteBuf buf) {
        this(buf.readUUID());
    }

    public static void toBytes(ClearNamePacket msg, FriendlyByteBuf buf) {
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
        if (ModMain.namePlates.get(msg.uuid) != null){
            ModMain.namePlates.get(msg.uuid).remove(Entity.RemovalReason.DISCARDED);
            ModMain.namePlates.remove(msg.uuid);
        }
    }
}

