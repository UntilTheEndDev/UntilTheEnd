package HamsterYDS.UntilTheEnd.crops;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Crops {
    static YamlConfiguration yaml;

    public Crops(UntilTheEnd plugin) {
        yaml = Config.autoUpdateConfigs("crops.yml");
        System.out.println("[UntilTheEnd]正在加载农作物计算模块......");
        CropProvider.loadConfig();
        if (plugin.getConfig().getBoolean("crops.season"))
            new SeasonCroping(plugin);
        if (plugin.getConfig().getBoolean("crops.takewaterup.enable"))
            new TakeWaterUp(plugin);
    }
}
