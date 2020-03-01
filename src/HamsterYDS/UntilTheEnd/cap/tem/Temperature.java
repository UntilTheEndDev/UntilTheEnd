package HamsterYDS.UntilTheEnd.cap.tem;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Temperature {
	public static UntilTheEnd plugin; 
	public static YamlConfiguration yaml;
	public Temperature(UntilTheEnd plugin) {
		this.plugin=plugin;
		Config.autoUpdateConfigs("temperature.yml");
		File file=new File(plugin.getDataFolder(),"temperature.yml");
		if(!file.exists()) plugin.saveResource("temperature.yml",true);
		yaml=YamlConfiguration.loadConfiguration(file);
		System.out.println("[UntilTheEnd]正在加载温度计算模块......");
		new TemperatureProvider(plugin);
		new ChangeTasks(plugin);
		new InfluenceTasks(plugin);
	}
}
