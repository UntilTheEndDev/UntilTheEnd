package HamsterYDS.UntilTheEnd.cap.hum;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class WeatherTask extends BukkitRunnable{
	public static UntilTheEnd plugin;
	public WeatherTask(UntilTheEnd plugin) {
		this.plugin=plugin;
		runTaskTimer(plugin,0L,150L); 
	}
	@Override
	public void run() {
		for(World world:Bukkit.getWorlds()) {
			if(Config.disableWorlds.contains(world.getName())) continue;
			if(world.hasStorm())
				for(Player player:world.getPlayers()) 
					if(!hasShelter(player)) {
						if(player.getItemInHand()!=null) 
							if(isUmbrella(player,player.getItemInHand())){
								PlayerManager.change(player.getName(),"hum",-1);
								continue;
							}
						if(!players.contains(player.getName())) {
							int factor=1;
							new DelayTask1(factor,player);
						}
					}
						
			if(!world.hasStorm())
				for(Player player:world.getPlayers()) 
					PlayerManager.change(player.getName(),"hum",-1);
		}
	}
	public static boolean isUmbrella(Player player,ItemStack item) {
		ItemStack helmet=player.getInventory().getHelmet();
		if(item.getItemMeta()!=null) 
			if(item.getItemMeta().getDisplayName()!=null) {
				if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§6伞")) 
					return true;
				if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§6花伞")) {
					if(Math.random()<=0.3)
						PlayerManager.change(player.getName(),"san",1);
					return true;
				}
			}
		if(helmet==null) return false;
		if(helmet.getItemMeta()!=null) 
			if(helmet.getItemMeta().getDisplayName()!=null) 
				if(helmet.getItemMeta().getDisplayName().equalsIgnoreCase("§6眼球伞")) {
					if(Math.random()<=0.75)
						PlayerManager.change(player.getName(),"san",2);
					return true;
				}
		return false;
	}
	public boolean hasShelter(Player player) {
		Location loc=player.getLocation();
		for(int i=0;i<=100;i++) {
			loc=loc.add(0,1.0,0);
			if(player.getWorld().getBlockAt(loc).getType()!=Material.AIR) 
				return true;
		}
		return false;
	}
	private static ArrayList<String> players=new ArrayList<String>();
	public class DelayTask1 extends BukkitRunnable{
		int counter=0;
		Player player;
		@Override
		public void run() {
			if(counter<=0) {
				PlayerManager.change(player.getName(),"hum",1);
				cancel();
				players.remove(player.getName());
			}
			counter--;
		}
		public DelayTask1(int delay,Player player) {
			this.counter=delay;
			this.player=player;
			players.add(player.getName());
			runTaskTimer(plugin,0L,20L);
		}
	}
}
