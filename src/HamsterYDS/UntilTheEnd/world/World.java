package HamsterYDS.UntilTheEnd.world;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class World {
	public static UntilTheEnd plugin;
	public World() {}
	public World(UntilTheEnd plugin) {
		this.plugin=plugin;
		new WorldState(plugin);
		if(plugin.getConfig().getBoolean("world.darkness.enable"))
			new Influence(plugin);
		if(plugin.getConfig().getBoolean("world.blind.enable"))
			new WorldTime(plugin);
	}
}
