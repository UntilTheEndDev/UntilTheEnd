package HamsterYDS.UntilTheEnd.cap.tem;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Temperature {
	public static UntilTheEnd plugin;
	public Temperature(UntilTheEnd plugin) {
		this.plugin=plugin;
		new NaturalTemperature();
		if(plugin.getConfig().getBoolean("tem.influence.natural"))
			new NaturalTask(plugin);
		if(plugin.getConfig().getBoolean("tem.influence.hum"))
			new HumidityTask(plugin);
		new BlockTemperature(plugin);
		new Influence(plugin);
	}
}
