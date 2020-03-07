package HamsterYDS.UntilTheEnd.cap.san;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

public class ChangeTasks {
	public UntilTheEnd plugin;
	public static double auraRangeX=Sanity.yaml.getDouble("auraRangeX");
	public static double auraRangeY=Sanity.yaml.getDouble("auraRangeY");
	public static double auraRangeZ=Sanity.yaml.getDouble("auraRangeZ");
	public static int monsterChangeSanity=Sanity.yaml.getInt("monsterChangeSanity");
	public static int playerChangeSanity=Sanity.yaml.getInt("playerChangeSanity");
	public static int auraPeriod=Sanity.yaml.getInt("auraPeriod");
	public static int humidityPeriod=Sanity.yaml.getInt("humidityPeriod");
	public static int timePeriod=Sanity.yaml.getInt("timePeriod");
	public static int evening=Sanity.yaml.getInt("evening");
	public static int night=Sanity.yaml.getInt("night");
	public static int day=Sanity.yaml.getInt("day");
	public static HashMap<String,Integer> clothesChangeSanity=new HashMap<String,Integer>();
	public static HashMap<String,Integer> itemsChangeSanity=new HashMap<String,Integer>();
	public ChangeTasks(UntilTheEnd plugin) {
		this.plugin=plugin;
		new ClothesTasks().runTaskTimer(plugin,0L,200L);
		new ItemsTasks().runTaskTimer(plugin,0L,200L);
		new SanityAura().runTaskTimer(plugin,0L,auraPeriod);
		new HumidityTask().runTaskTimer(plugin,0L,humidityPeriod);
		new TimeTask().runTaskTimer(plugin,0L,timePeriod);
	}
	public class ClothesTasks extends BukkitRunnable{
		@Override
		public void run() {
			for(World world:Config.enableWorlds) {
				for(Player player:world.getPlayers()) {
					PlayerInventory inv=player.getInventory();
					String helmet=getName(inv.getHelmet());
					String chestplate=getName(inv.getChestplate());
					String leggings=getName(inv.getLeggings());
					String boots=getName(inv.getBoots()); 
					if(clothesChangeSanity.containsKey(helmet))
						PlayerManager.change(player,"san",clothesChangeSanity.get(helmet));
					if(clothesChangeSanity.containsKey(chestplate))
						PlayerManager.change(player,"san",clothesChangeSanity.get(chestplate));
					if(clothesChangeSanity.containsKey(leggings))
						PlayerManager.change(player,"san",clothesChangeSanity.get(leggings));
					if(clothesChangeSanity.containsKey(boots))
						PlayerManager.change(player,"san",clothesChangeSanity.get(boots));
				}
			}
		}
		public String getName(ItemStack item) {
			if(item!=null)
				if(item.hasItemMeta()) 
					if(item.getItemMeta().hasDisplayName()) 
						return item.getItemMeta().getDisplayName();
			return "";
		}
	}
	public class ItemsTasks extends BukkitRunnable{
		@Override
		public void run() {
			for(World world:Config.enableWorlds) {
				for(Player player:world.getPlayers()) {
					PlayerInventory inv=player.getInventory();
					for(int slot=0;slot<inv.getSize();slot++) {
						ItemStack item=inv.getItem(slot);
						String itemName=getName(item);
						if(itemsChangeSanity.containsKey(itemName))
							PlayerManager.change(player,"san",item.getAmount()*itemsChangeSanity.get(itemName));
					}
				}
			}
		}
		public String getName(ItemStack item) {
			if(item!=null)
				if(item.hasItemMeta()) 
					if(item.getItemMeta().hasDisplayName()) 
						return item.getItemMeta().getDisplayName();
			return "";
		}
	}
	public class SanityAura extends BukkitRunnable{
		@Override
		public void run() {
			for(World world:Config.enableWorlds) 
				for(Player player:world.getPlayers()) 
					for(Entity entity:player.getNearbyEntities(auraRangeX,auraRangeY,auraRangeZ)) {
						EntityType type=entity.getType();
						if(SanityProvider.creatureAura.containsKey(type)) 
							PlayerManager.change(player,"san",SanityProvider.creatureAura.get(type));
						if(entity instanceof Player) PlayerManager.change(player,"san",playerChangeSanity);
						if(entity instanceof Monster) PlayerManager.change(player,"san",monsterChangeSanity);
					}
		}
	}
	public class HumidityTask extends BukkitRunnable{
		@Override
		public void run() {
			for(World world:Config.enableWorlds) {
				for(Player player:world.getPlayers()) {
					int hum=PlayerManager.check(player,"hum");
					PlayerManager.change(player,"san",-hum/10);
				}
			}
		}
	}
	public class TimeTask extends BukkitRunnable{
		public long counter=0;
		@Override
		public void run() {
			counter++;
			for(World world:Config.enableWorlds) {
				long time=world.getTime();
				if(counter%2==0)
					if(time>=evening&&time<=night)
						for(Player player:world.getPlayers()) 
							PlayerManager.change(player,"san",-1);
				if(time>=night&&time<=day)
					for(Player player:world.getPlayers()) 
						PlayerManager.change(player,"san",-1);
			}
		}
	}
}
