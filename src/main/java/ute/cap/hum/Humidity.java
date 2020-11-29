package ute.cap.hum;

import ute.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import ute.Config;
import ute.UntilTheEnd;

public class Humidity implements Listener {
    public static YamlConfiguration yaml;

    public static void initialize(UntilTheEnd plugin) {
        yaml = Config.autoUpdateConfigs("humidity.yml");
        Bukkit.getConsoleSender().sendMessage(UTEi18n.cacheWithPrefix("cap.hum.provider.loading"));
        HumidityProvider.loadConfig();
        ChangeTasks.initialize(plugin);
        InfluenceTasks.initialize(plugin);
        new InfluenceEvents(plugin);
    }
}
