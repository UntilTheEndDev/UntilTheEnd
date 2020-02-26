package HamsterYDS.UntilTheEnd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	public static UntilTheEnd plugin;
	public static YamlConfiguration yaml=null; 
	public static List<World> enableWorlds=new ArrayList<World>();
	public Config(UntilTheEnd plugin) {
		this.plugin=plugin;
		for(String worldName:plugin.getConfig().getStringList("enableWorlds")) 
			enableWorlds.add(Bukkit.getWorld(worldName));
		File file=new File(plugin.getDataFolder(),plugin.getConfig().getString("language"));
		yaml=YamlConfiguration.loadConfiguration(file);
	}
	public static String getLang(String path) {
		return yaml.getString(path);
	}
	//TODO
	public static void autoUpdateConfigs() {
		File file=new File(plugin.getDataFolder()+"/confUpdater");
		file.mkdir();
		
	}
}
