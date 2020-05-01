package HamsterYDS.UntilTheEnd.cap.hum;

import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
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
