package ute.crops;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import ute.Config;
import ute.internal.UTEi18n;

public class Crops {
    static YamlConfiguration yaml;
    public static void initialize() {
        yaml = Config.autoUpdateConfigs("crops.yml");
        Bukkit.getServer().getConsoleSender().sendMessage(UTEi18n.cacheWithPrefix("cap.crops.provider.loading"));
        CropProvider.loadConfig();
        if (yaml.getBoolean("crops.season"))
            new SeasonCroping();
        if (yaml.getBoolean("crops.takewaterup.enable"))
            new TakeWaterUp();
    }
}
