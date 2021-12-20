package ca.lukegrahamlandry.findmyfriends.entity;

import ca.lukegrahamlandry.findmyfriends.ClientFindConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;


// will only exist on the client
public class NamePlateEntity extends Entity {
    public Vector3d targetPosition;
    public double dist = 0;
    public UUID targetUUID;
    public boolean showDist = true;

    public NamePlateEntity(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) remove();
        else this.updateLocation();
    }

    @OnlyIn(Dist.CLIENT)
    public void updateLocation() {
        // move to the right place relitive to the viewer
        PlayerEntity player = Minecraft.getInstance().player;
        Vector3d direction = targetPosition.subtract(player.position());
        this.dist = direction.length();
        if (this.dist <= ClientFindConfig.getFakeNameDisplayDistance()){
            this.setPos(targetPosition.x, targetPosition.y, targetPosition.z);
        } else {
            direction = direction.normalize().scale(ClientFindConfig.getFakeNameDisplayDistance());
            Vector3d position = player.position().add(direction);
            this.setPos(position.x, position.y, position.z);
        }

    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {

    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return null;
    }

    @Override
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        return true;
    }
}
