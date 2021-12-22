package ca.lukegrahamlandry.findmyfriends.entity;

import ca.lukegrahamlandry.findmyfriends.ClientFindConfig;
import ca.lukegrahamlandry.findmyfriends.ModMain;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;


// will only exist on the client
public class NamePlateEntity extends Entity {
    public Vec3 targetPosition;
    public double dist = 0;
    public UUID targetUUID;
    public boolean showDist = true;

    public NamePlateEntity(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) remove(RemovalReason.DISCARDED);
        else {
            this.updateLocation();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void updateLocation() {
        // when loaded on client, no lag time
        Player targetPlayer = this.level.getPlayerByUUID(this.targetUUID);
        if (targetPlayer != null){
            this.targetPosition = targetPlayer.position();
        }

        // move to the right place relitive to the viewer
        Player player = Minecraft.getInstance().player;
        Vec3 direction = targetPosition.subtract(player.position());
        this.dist = direction.length();
        if (this.dist <= ClientFindConfig.getFakeNameDisplayDistance()){
            this.setPos(targetPosition.x, targetPosition.y, targetPosition.z);
        } else {
            direction = direction.normalize().scale(ClientFindConfig.getFakeNameDisplayDistance());
            Vec3 position = player.position().add(direction);
            this.setPos(position.x, position.y, position.z);
        }
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_70037_1_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_213281_1_) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return null;
    }

    @Override
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        return true;
    }
}
