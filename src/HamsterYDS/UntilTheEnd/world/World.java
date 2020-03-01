package HamsterYDS.UntilTheEnd.world;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class World {
	public static UntilTheEnd plugin;
	public static File file;
	public static YamlConfiguration yaml;
	public World(UntilTheEnd plugin) {
		this.plugin=plugin;
		file=new File(plugin.getDataFolder(),"worlds.yml");
		yaml=YamlConfiguration.loadConfiguration(file);
		WorldProvider.loadWorlds();
		WorldProvider.saveWorlds();
		new WorldCounter().runTaskTimer(plugin,0L,20L);
		new InfluenceTasks(plugin);
	} 
}
