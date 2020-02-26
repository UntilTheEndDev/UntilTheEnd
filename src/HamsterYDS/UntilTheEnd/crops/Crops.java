package HamsterYDS.UntilTheEnd.crops;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Crops {
	public static UntilTheEnd plugin;
	public Crops(UntilTheEnd plugin) {
		this.plugin=plugin;
		if(plugin.getConfig().getBoolean("crops.season"))
			new SeasonCroping(plugin);
		if(plugin.getConfig().getBoolean("crops.takewaterup.enable"))
			new TakeWaterUp(plugin);
	}
}
