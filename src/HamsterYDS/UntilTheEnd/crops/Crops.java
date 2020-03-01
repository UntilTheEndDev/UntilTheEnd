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
	public static UntilTheEnd plugin;
	static YamlConfiguration yaml;
	public Crops(UntilTheEnd plugin) {
		this.plugin=plugin;
		Config.autoUpdateConfigs("crops.yml");
		File file=new File(plugin.getDataFolder(),"crops.yml");
		plugin.saveResource("crops.yml",true);
		yaml=YamlConfiguration.loadConfiguration(file);
		System.out.println("[UntilTheEnd]正在加载农作物计算模块......");
		CropProvider.loadConfig();
		if(plugin.getConfig().getBoolean("crops.season"))
			new SeasonCroping(plugin);
		if(plugin.getConfig().getBoolean("crops.takewaterup.enable"))
			new TakeWaterUp(plugin);
	}
}
