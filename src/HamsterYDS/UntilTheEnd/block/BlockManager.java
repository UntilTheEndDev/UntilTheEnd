package HamsterYDS.UntilTheEnd.block;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BlockManager extends BukkitRunnable implements Listener{
	public static UntilTheEnd plugin;
	public static HashMap<String,String> blocks=new HashMap<String,String>();
	public static HashMap<String,ArrayList<String>> blockDatas=new HashMap<String,ArrayList<String>>();
	
	public BlockManager(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		runTaskTimer(plugin,0L,plugin.getConfig().getLong("block.fresh")*20);
		File file=new File(plugin.getDataFolder()+"/data/","blocks.yml");
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		for(String path:yaml.getKeys(false)) {
			blocks.put(path,yaml.getString(path));
			addBlockData(yaml.getString(path),path);
		}
		System.out.println(blockDatas);
	}
	public static void addBlockData(String blockName,String toString) {
		ArrayList<String> array;
		if(blockDatas.containsKey(blockName))
				array=blockDatas.get(blockName);
		else array=new ArrayList<String>();
		array.add(toString);
		blockDatas.remove(blockName);
		blockDatas.put(blockName,array);
	}
	public static void removeBlockData(String blockName,String toString) {
		ArrayList<String> array;
		if(blockDatas.containsKey(blockName))
				array=blockDatas.get(blockName);
		else array=new ArrayList<String>();
		array.remove(toString);
		blockDatas.remove(blockName);
		blockDatas.put(blockName,array);
	}
	public static void saveBlocks() {
		File file=new File(plugin.getDataFolder()+"/data/","blocks.yml");
		file.delete();
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		for(String loc:blocks.keySet()) {
			String item=blocks.get(loc);
			yaml.set(loc,item);
		}
		try {
			yaml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void loadBlocks() {
		File file=new File(plugin.getDataFolder()+"/data/","blocks.yml");
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		for(String path:yaml.getKeys(false)) {
			blocks.remove(path);
			blocks.put(path,yaml.getString(path));
			addBlockData(yaml.getString(path),path);
		}
	}
	@EventHandler public void onBreak(BlockBreakEvent event) {
		if(event.isCancelled()) return;
		Location loc=event.getBlock().getLocation();
		String toString=BlockApi.locToStr(loc);
		if(blocks.get(toString)==null) return; 
		event.setDropItems(false);
		loc.getWorld().dropItemNaturally(loc,ItemProvider.items.get(blocks.get(toString)));
		removeBlockData(blocks.get(toString),toString);
		blocks.remove(toString);
	}
	@EventHandler public void onPlace(BlockPlaceEvent event) {
		if(event.isCancelled()) return;
		if(event.getItemInHand()==null) return;
		ItemStack item=event.getItemInHand();
		if(item.getItemMeta()==null) return;
		if(item.getItemMeta().getDisplayName()==null) return;
		Location loc=event.getBlock().getLocation();
		String toString=BlockApi.locToStr(loc);
		blocks.put(toString,ItemLoader.canPlace.get(item.getItemMeta().getDisplayName()));
		addBlockData(ItemLoader.canPlace.get(item.getItemMeta().getDisplayName()),toString);
	}
	@EventHandler public void onExtend(BlockPistonExtendEvent event) {
		if(event.isCancelled()) return;
		for(org.bukkit.block.Block block:event.getBlocks()) {
			Location loc=block.getLocation();
			String toString=BlockApi.locToStr(loc);
			if(blocks.containsKey(toString)) event.setCancelled(true); 
		}
	}
	@Override
	public void run() {
		saveBlocks();
	}
}
