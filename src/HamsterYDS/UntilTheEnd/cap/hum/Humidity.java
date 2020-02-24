package HamsterYDS.UntilTheEnd.cap.hum;

import org.bukkit.event.Listener;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Humidity implements Listener{
	public static UntilTheEnd plugin;
	public Humidity() {}
	public Humidity(UntilTheEnd plugin) {
		this.plugin=plugin;
		if(plugin.getConfig().getBoolean("hum.influence.state"))
			new StateTask(plugin);
		if(plugin.getConfig().getBoolean("hum.influence.weather"))
			new WeatherTask(plugin);
		if(plugin.getConfig().getBoolean("hum.moistness"))
			new Moistness(plugin);
		new Influencer(plugin);
	}
}
