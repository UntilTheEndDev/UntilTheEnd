package HamsterYDS.UntilTheEnd.cap.hum;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

public class ChangeTasks {
	public static UntilTheEnd plugin;
	public static ArrayList<String> umbrellas=new ArrayList<String>();
	public static ArrayList<String> waterProofSuits=new ArrayList<String>();
	public static long weahterChangePeriod=Humidity.yaml.getLong("weahterChangePeriod");
	public static long stateChangePeriod=Humidity.yaml.getLong("stateChangePeriod");
	public ChangeTasks(UntilTheEnd plugin) {
		this.plugin=plugin;
		new WeatherTask().runTaskTimer(plugin,0L,weahterChangePeriod);
		new StateTask().runTaskTimer(plugin,0L,stateChangePeriod);
	}
	public class WeatherTask extends BukkitRunnable{
		@Override
		public void run() {
			for(World world:Config.enableWorlds) {
				if(world.hasStorm()) {
					for(Player player:world.getPlayers()) {
						PlayerInventory inv=player.getInventory();
						ItemStack rightHand=inv.getItemInMainHand();
						ItemStack leftHand=inv.getItemInOffHand();
						if(isUmbrella(rightHand)||isUmbrella(leftHand)) continue;
						ItemStack[] armors=inv.getArmorContents();
						boolean hasSuit=false;
						for(ItemStack armor:armors) {
							if(isSuit(armor)) hasSuit=true;
						}
						if(hasSuit) continue;
						if(hasShelter(player)) continue;
						PlayerManager.change(player,"hum",1);
					}
				}else {
					for(Player player:world.getPlayers()) 
						PlayerManager.change(player,"hum",-1);
				}
			}
		}
		public boolean isUmbrella(ItemStack item) {
			if(item==null) return false;
			if(item.hasItemMeta())
				if(item.getItemMeta().hasDisplayName())
					if(umbrellas.contains(item.getItemMeta().getDisplayName()))
						return true;
			return false;
		}
		public boolean isSuit(ItemStack item) {
			if(item==null) return false;
			if(item.hasItemMeta())
				if(item.getItemMeta().hasDisplayName())
					if(waterProofSuits.contains(item.getItemMeta().getDisplayName()))
						return true;
			return false;
		}
		public boolean hasShelter(Player player) {
			Location loc=player.getLocation();
			for(int i=0;i<=100;loc=loc.add(0,1.0,0),i++) 
				if(player.getWorld().getBlockAt(loc).getType()!=Material.AIR) 
					return true;
			return false;
		}
	}
	public class StateTask extends BukkitRunnable{
		@Override
		public void run() {
			for(World world:Config.enableWorlds)
				for(Player player:world.getPlayers()) {
					Location loc=player.getLocation();
					if(world.getBlockAt(loc).getType().equals(Material.WATER)||world.getBlockAt(loc).getType().equals(Material.STATIONARY_WATER)) 
						PlayerManager.change(player,"hum",1);
				}
		}
	}
}
