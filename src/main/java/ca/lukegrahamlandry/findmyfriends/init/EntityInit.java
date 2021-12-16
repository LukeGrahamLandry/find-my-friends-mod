package ca.lukegrahamlandry.findmyfriends.init;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import ca.lukegrahamlandry.findmyfriends.entity.NamePlateEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITIES, ModMain.MOD_ID);

    public static final RegistryObject<EntityType<NamePlateEntity>> NAME_PLATE = ENTITY.register("name_plate", () -> EntityType.Builder.of(NamePlateEntity::new, EntityClassification.CREATURE).sized(0.25F, 0.25F).build("name_plate"));
}