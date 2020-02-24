package HamsterYDS.UntilTheEnd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	public static UntilTheEnd plugin;
	public static YamlConfiguration yaml=null; 
	public static List<String> disableWorlds=new ArrayList<String>();
	public Config(UntilTheEnd plugin) {
		this.plugin=plugin;
		File file=new File(plugin.getDataFolder(),plugin.getConfig().getString("language"));
		yaml=YamlConfiguration.loadConfiguration(file);
		disableWorlds=plugin.getConfig().getStringList("disableWorlds");
	}
	public static String getLang(String path) {
		return yaml.getString(path);
	}
}
