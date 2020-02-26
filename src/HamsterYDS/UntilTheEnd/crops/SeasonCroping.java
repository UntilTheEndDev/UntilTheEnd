package HamsterYDS.UntilTheEnd.crops;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.world.WorldState;
import HamsterYDS.UntilTheEnd.world.WorldState.Season;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class SeasonCroping implements Listener{
	public static UntilTheEnd plugin;
	public static HashMap<String,HashMap<String,Double>> crops=new HashMap<String,HashMap<String,Double>>();
	public SeasonCroping(UntilTheEnd plugin) {
		this.plugin=plugin;
		loadConfig();
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	public static void loadConfig() {
		File file=new File(plugin.getDataFolder(),"crops.yml");
		plugin.saveResource("crops.yml",true);
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		for(String path:yaml.getKeys(false)) {
			List<String> seasons=yaml.getStringList(path+".seasons");
			List<Double> percents=yaml.getDoubleList(path+".percents");
			HashMap<String,Double> crop=new HashMap<String,Double>();
			int index=0;
			for(String name:seasons) {
				crop.put(name,percents.get(index));
				index++;
			}
			crops.put(path,crop);
		}
	}
	@EventHandler(priority=EventPriority.LOW) public void onGrow(BlockGrowEvent event) {
		if(event.getBlock()==null) return;
		Block block=event.getBlock();
		
		World world=block.getWorld();
		Season season=WorldState.worldStates.get(world.getName()).season;
		String seasonName=season.toString();
		
		Material material=block.getState().getData().getItemType();
		String name=material.toString();
		if(crops.containsKey(name)) {
			HashMap<String,Double> crop=crops.get(name);
			if(crop.containsKey(seasonName)) {
				double percent=crop.get(seasonName);
				if(Math.random()>percent) event.setCancelled(true);
			}else event.setCancelled(true);
		}
	}
}
