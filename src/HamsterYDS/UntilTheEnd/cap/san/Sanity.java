package HamsterYDS.UntilTheEnd.cap.san;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Sanity {
	public static UntilTheEnd plugin;
	public Sanity() {}
	public Sanity(UntilTheEnd plugin) {
		this.plugin=plugin;
		if(plugin.getConfig().getBoolean("san.influence.time"))
			new TimeTask(plugin);
		if(plugin.getConfig().getBoolean("san.influence.hum"))
			new HumidityTask(plugin);
		if(plugin.getConfig().getBoolean("san.influence.clothes"))
			new ClothesTask(plugin);
		if(plugin.getConfig().getBoolean("san.influence.entity"))
			new EntityTask(plugin);
		if(plugin.getConfig().getBoolean("san.influence.nightmare"))
			new NightMareTask(plugin);
		new Influencer(plugin);
	}
}
