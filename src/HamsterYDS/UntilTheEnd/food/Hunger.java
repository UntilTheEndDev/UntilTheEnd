package HamsterYDS.UntilTheEnd.food;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Hunger extends BukkitRunnable{
	private static long counter=0;
	public static UntilTheEnd plugin;
	int cd=0;
	public Hunger(UntilTheEnd plugin) {
		this.plugin=plugin;
		cd=plugin.getConfig().getInt("food.hunger.speed");
		runTaskTimer(plugin,0L,20L);
	}
	@Override
	public void run() {
		counter++;
		for(Player player:Bukkit.getOnlinePlayers()) {
			if(Config.disableWorlds.contains(player.getWorld().getName())) continue;
			if(player.getFoodLevel()<=1.0) 
				player.damage(0.5);
		}		
		if(counter%cd==0) 
			for(Player player:Bukkit.getOnlinePlayers()){
				if(Config.disableWorlds.contains(player.getWorld().getName())) continue;
				player.setFoodLevel(player.getFoodLevel()-1);
			}
	}

}
