package ute.cap.tem;

import ute.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import ute.Config;
import ute.UntilTheEnd;

public class Temperature {
    public static YamlConfiguration yaml;

    public static void initialize(UntilTheEnd plugin) {
        yaml = Config.autoUpdateConfigs("temperature.yml");
        Bukkit.getConsoleSender().sendMessage(UTEi18n.cacheWithPrefix("cap.tem.provider.loading"));
        TemperatureProvider.initialize(plugin);
        ChangeTasks.initialize(plugin);
        InfluenceTasks.initialize(plugin);
    }
}
