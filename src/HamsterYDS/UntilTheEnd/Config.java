package HamsterYDS.UntilTheEnd;

import java.io.File;
import java.io.IOException;
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
		enableWorlds=Bukkit.getWorlds();
		for(String worldName:plugin.getConfig().getStringList("disableWorlds")) 
			for(World world:Bukkit.getWorlds()) 
				if(worldName.equalsIgnoreCase(world.getName())) 
					enableWorlds.remove(world);
		File file=new File(plugin.getDataFolder(),plugin.getConfig().getString("language"));
		yaml=YamlConfiguration.loadConfiguration(file);
		autoUpdateConfigs("config.yml");
	}
	public static String getLang(String path) {
		return yaml.getString(path);
	}
	public static void autoUpdateConfigs(String name) {
		File file=new File(plugin.getDataFolder(),name);
		final YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		file.delete();
		plugin.saveResource(name,true);
		YamlConfiguration newYaml=YamlConfiguration.loadConfiguration(file);
		for(String path:yaml.getKeys(true)) {
			Object obj=yaml.get(path);
			newYaml.set(path,obj);
		}
		try {
			newYaml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
