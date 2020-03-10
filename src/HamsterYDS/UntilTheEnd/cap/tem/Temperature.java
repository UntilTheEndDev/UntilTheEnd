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

    public Temperature(UntilTheEnd plugin) {
        yaml = Config.autoUpdateConfigs("temperature.yml");
        Bukkit.getConsoleSender().sendMessage(UTEi18n.cacheWithPrefix("cap.tem.provider.loading"));
        new TemperatureProvider(plugin);
        new ChangeTasks(plugin);
        new InfluenceTasks(plugin);
    }
}
