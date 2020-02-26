package HamsterYDS.UntilTheEnd.cap.tiredness;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

public class InfluenceEvents implements Listener{
	public static UntilTheEnd plugin;
	public InfluenceEvents(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	@EventHandler public void onRun(PlayerInteractEvent event) {
		
	}
}
