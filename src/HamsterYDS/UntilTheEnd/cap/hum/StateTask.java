package HamsterYDS.UntilTheEnd.cap.hum;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class StateTask extends BukkitRunnable{
	public static UntilTheEnd plugin;
	public StateTask(UntilTheEnd plugin) {
		this.plugin=plugin;
		runTaskTimer(plugin,0L,50L); 
	}
	@Override
	public void run() {
		for(Player player:Bukkit.getOnlinePlayers()) {
			if(Config.disableWorlds.contains(player.getWorld().getName())) continue;
			World world=player.getWorld();
			Location loc=player.getLocation();
			if(world.getBlockAt(loc).getType().equals(Material.WATER)||world.getBlockAt(loc).getType().equals(Material.STATIONARY_WATER)) 
				PlayerManager.change(player.getName(),"hum",1);
		}
	}
}