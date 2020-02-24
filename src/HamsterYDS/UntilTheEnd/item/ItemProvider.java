package HamsterYDS.UntilTheEnd.item;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class ItemProvider implements Listener{
	public static HashMap<EntityType,String> drops=new HashMap<EntityType,String>();
	public static HashMap<EntityType,Double> percents=new HashMap<EntityType,Double>();
	public static HashMap<String,ItemStack> items=new HashMap<String,ItemStack>();
	public static UntilTheEnd plugin;
	public ItemProvider(UntilTheEnd plugin) {
		this.plugin=plugin;
		loadConfig();
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	public static void loadConfig() {
		File file=new File(plugin.getDataFolder(),"drops.yml");
		plugin.saveResource("drops.yml",true);
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		for(String path:yaml.getKeys(false)) {
			EntityType type=EntityType.fromName(path);
			String itemName=yaml.getString(path+".dropitem");
			double percent=yaml.getDouble(path+".percent");
			drops.put(type,itemName);
			percents.put(type,percent);
		}
	}
	@EventHandler public void onDeath(EntityDeathEvent event) {
		EntityType type=event.getEntityType();
		if(!drops.containsKey(type)) return;
		String itemName=drops.get(type);
		double percent=percents.get(type);
		ItemStack item=getItem(itemName);
		if(item==null) return;
		dropItem(event.getEntity().getWorld(),event.getEntity().getLocation(),item,percent);
	}
	public static ItemStack getItem(String itemName) {
		return items.get(itemName);
	}
	public static void addItem(Class<?> itemClass,ItemStack item) {
		items.put(itemClass.getSimpleName(),item);
	}
	public static void dropItem(World world,Location loc,ItemStack item,double percent) {
		while(percent>=1.0) {
			percent-=1.0;
			world.dropItemNaturally(loc,item);
		}
		if(Math.random()<=percent) 
			world.dropItemNaturally(loc,item);
	}
}
