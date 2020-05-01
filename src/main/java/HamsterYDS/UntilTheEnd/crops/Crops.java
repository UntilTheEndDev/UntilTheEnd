package HamsterYDS.UntilTheEnd.crops;

import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Crops {
    static YamlConfiguration yaml;

    public static void initialize(UntilTheEnd plugin) {
        yaml = Config.autoUpdateConfigs("crops.yml");
        Bukkit.getServer().getConsoleSender().sendMessage(UTEi18n.cacheWithPrefix("cap.crops.provider.loading"));
        CropProvider.loadConfig();
        if (plugin.getConfig().getBoolean("crops.season"))
            new SeasonCroping(plugin);
        if (plugin.getConfig().getBoolean("crops.takewaterup.enable"))
            new TakeWaterUp(plugin);
    }
}
