package HamsterYDS.UntilTheEnd.world;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
//import HamsterYDS.UntilTheEnd.world.nms.DarkNight;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class World {
    public static File file;
    public static YamlConfiguration yaml;

    public static void initialize(UntilTheEnd plugin) {
        file = new File(plugin.getDataFolder(), "worlds.yml");
        yaml = YamlConfiguration.loadConfiguration(file);
        WorldProvider.loadWorlds();
        new WorldCounter().runTaskTimer(plugin, 0L, 20L);
        InfluenceTasks.initialize(plugin);
//		new DarkNight().runTaskTimer(plugin,0L,50L);
    }
}
