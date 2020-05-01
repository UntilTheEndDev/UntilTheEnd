package HamsterYDS.UntilTheEnd.cap.tem;

import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
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
