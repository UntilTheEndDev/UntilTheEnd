package HamsterYDS.UntilTheEnd.player;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Player {
	public static UntilTheEnd plugin;
	public Player(UntilTheEnd plugin) {
		this.plugin=plugin;
		if(plugin.getConfig().getBoolean("player.inventory"))
			new PlayerInventoryAdapt(plugin);
		new PlayerManager(plugin);
		if(plugin.getConfig().getBoolean("player.death.enable"))
			new PlayerDeath(plugin);
	}
}
