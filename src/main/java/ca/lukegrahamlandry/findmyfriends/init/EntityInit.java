package ca.lukegrahamlandry.findmyfriends.init;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.entity.NamePlateEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITIES, ModMain.MOD_ID);

    public static final RegistryObject<EntityType<NamePlateEntity>> NAME_PLATE = ENTITY.register("name_plate", () -> EntityType.Builder.of(NamePlateEntity::new, MobCategory.CREATURE).sized(EntityType.PLAYER.getWidth(), EntityType.PLAYER.getHeight()).build("name_plate"));
}