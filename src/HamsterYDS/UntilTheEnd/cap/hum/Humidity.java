package HamsterYDS.UntilTheEnd.cap.hum;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Humidity implements Listener{
	public static UntilTheEnd plugin;
	public static YamlConfiguration yaml;
	public Humidity(UntilTheEnd plugin) {
		this.plugin=plugin;
		Config.autoUpdateConfigs("humidity.yml");
		File file=new File(plugin.getDataFolder(),"humidity.yml");
		if(!file.exists()) plugin.saveResource("humidity.yml",true);
		yaml=YamlConfiguration.loadConfiguration(file);
		System.out.println("[UntilTheEnd]正在加载湿度计算模块......");
		HumidityProvider.loadConfig();
		new ChangeTasks(plugin);
		new InfluenceTasks(plugin);
		new InfluenceEvents(plugin);
	}
}
