package HamsterYDS.UntilTheEnd.food;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Hunger implements Listener{
	public static UntilTheEnd plugin;
	public Hunger(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		new AutoHunger().runTaskTimer(plugin,0L,20*plugin.getConfig().getLong("food.hunger.speed"));
	}
	@EventHandler public void onJoin(PlayerJoinEvent event) {
		event.getPlayer().setFoodLevel(10);
	}
	public class AutoHunger extends BukkitRunnable{
		@Override
		public void run() {
			for(World world:Config.enableWorlds)
				for(Player player:world.getPlayers()) 
					if(player.getGameMode()==GameMode.CREATIVE||player.getGameMode()==GameMode.SPECTATOR) continue;
					else player.setFoodLevel(player.getFoodLevel()-1);
		}
	}
}
