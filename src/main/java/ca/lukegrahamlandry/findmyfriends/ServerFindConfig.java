package ca.lukegrahamlandry.findmyfriends;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class ServerFindConfig {
    public static final ForgeConfigSpec server_config;

    public static final ForgeConfigSpec.BooleanValue hideSneakingPlayers;
    public static final ForgeConfigSpec.BooleanValue showDistance;
    public static final ForgeConfigSpec.DoubleValue maxDistance;
    public static final ForgeConfigSpec.IntValue updateInterval;

    static {
        final ForgeConfigSpec.Builder serverBuilder = new ForgeConfigSpec.Builder();

        serverBuilder.comment("Find My Friends server side settings")
                .push("server");

        hideSneakingPlayers = serverBuilder
                .comment("when true, the name tags will not show for players holding shift (like vanilla)")
                .define("hideSneakingPlayers", true);

        showDistance = serverBuilder
                .comment("when true, the distance to a player will be shown above their name (when they're beyond vanilla's normal range)")
                .define("showDistance", true);

        maxDistance = serverBuilder
                .comment("Name tags will not be shown for players beyond this distance. Use -1 for infinity")
                .defineInRange("maxDistance", -1, -1, Double.MAX_VALUE);

        updateInterval = serverBuilder
                .comment("Player positions will be synced to clients every x ticks. Higher numbers will cause less lag but reduce accuracy")
                .defineInRange("updateInterval", 20, 1, Integer.MAX_VALUE);

        server_config = serverBuilder.build();
    }

    public static void init(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server_config);
        CommentedFileConfig file = CommentedFileConfig.builder(new File(FMLPaths.CONFIGDIR.get().resolve(ModMain.MOD_ID + ".toml").toString())).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        server_config.setConfig(file);
    }

    public static int getUpdateInterval() {
        return updateInterval.get();
    }
}
