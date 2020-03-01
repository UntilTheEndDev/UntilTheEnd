package HamsterYDS.UntilTheEnd.player;

import org.bukkit.Bukkit;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Player {
	public static UntilTheEnd plugin;
	public Player(UntilTheEnd plugin) {
		this.plugin=plugin;
		new PlayerManager(plugin);
		for(org.bukkit.entity.Player player:Bukkit.getOnlinePlayers()) 
			PlayerManager.load(player.getName());
		new PlayerInventoryAdapt(plugin);
		new PlayerDeath(plugin);
	}
}
