package HamsterYDS.UntilTheEnd.cap.tem;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BlockTemperature {
	public static UntilTheEnd plugin;
	public static HashMap<Material,Integer> blockTemperatures=new HashMap<Material,Integer>();
	public BlockTemperature(UntilTheEnd plugin) {
		this.plugin=plugin;
		loadConfig();
	}
	public static void loadConfig() {
		File file=new File(plugin.getDataFolder(),"temperature.yml");
		plugin.saveResource("temperature.yml",true);
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		for(String path:yaml.getKeys(false)) {
			Material material=Material.getMaterial(path);
			int tem=yaml.getInt(path);
			blockTemperatures.put(material,tem);
		}
	}
	public static int getTemperature(Location loc) {
		if(!Config.enableWorlds.contains(loc.getWorld())) return 37;
		Block block=loc.getBlock();
		Material material;
		if(block==null) material=Material.AIR;
		else material=block.getType();
		if(blockTemperatures.containsKey(material)) return blockTemperatures.get(material);
		int wTem=NaturalTemperature.naturalTemperatures.get(loc.getWorld().getName());
		double tems=wTem;int tot=1;
		for(int x=-5;x<=5;x++) 
			for(int y=-5;y<=5;y++) 
				for(int z=-5;z<=5;z++) {
					Location newLoc=new Location(loc.getWorld(),loc.getX()+x,loc.getY()+y,loc.getZ()+z);
					if(newLoc.getBlock().getType()==Material.AIR) continue;
					Material blockMaterial=newLoc.getBlock().getType();
					double factor=loc.distance(newLoc);
					if(blockTemperatures.containsKey(blockMaterial)) {
						int blockTem=blockTemperatures.get(blockMaterial);
						int d_value=Math.abs(blockTem-wTem);
						int influent=(int) Math.pow(d_value/factor/2.3,1.5);
						tot++;
						if(blockTem>wTem) tems+=wTem+influent;
						else tems+=wTem-influent;
					}
				}
		int result=(int) (tems/tot);
		result=(int) (result-(1.6*((loc.getBlockY()-50)/10)));
		return result;
	}
}
