package HamsterYDS.UntilTheEnd.cap.tem;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.world.WorldState.Season;

public class TemperatureProvider {
	public static UntilTheEnd plugin;
	public static HashMap<World,Integer> worldTemperatures=new HashMap<World,Integer>();
	public static HashMap<Material,Integer> blockTemperatures=new HashMap<Material,Integer>();
	public TemperatureProvider(UntilTheEnd plugin) {
		this.plugin=plugin;
		loadWorldTemperatures();
		loadBlockTemperatures();
	}
	public static void loadWorldTemperatures() {
		for(World world:Bukkit.getWorlds()) {
			worldTemperatures.remove(world);
			if(Config.enableWorlds.contains(world)) worldTemperatures.put(world,getWorldTemperature(world));
			else worldTemperatures.put(world,37);
		}
	}
	public static void loadBlockTemperatures() {
		for(String path:Temperature.yaml.getKeys(true)) {
			if(path.equalsIgnoreCase("blockTemperature")) continue;
			if(path.startsWith("blockTemperature")) {
				int tem=Temperature.yaml.getInt(path);
				path=path.replace("blockTemperature.","");
				Material material=Material.getMaterial(path);
				System.out.println("检测到带有温度的方块"+path+"温度为："+tem);
				blockTemperatures.put(material,tem);
			}
		}
	}
	public static int getWorldTemperature(World world) {
		Season season=UntilTheEndApi.WorldApi.getSeason(world);
		int day=UntilTheEndApi.WorldApi.getDay(world);
		int temperature=37;
		switch(season) {
		case SPRING: {
			temperature=(int) (Math.random()*(-5)+Math.random()*5+1.5*day-Math.sqrt(Math.sqrt(Math.sqrt(world.getTime()))));
			break;
		}
		case SUMMER: {
			temperature=(int) (Math.random()*(-5)+Math.random()*15-0.2*day*day+4.6*day+35.5-Math.sqrt(Math.sqrt(Math.sqrt(world.getTime()))));
			break;
		}
		case AUTUMN: {
			temperature=(int) (Math.random()*(-5)+Math.random()*5+50-1.5*day-Math.sqrt(Math.sqrt(Math.sqrt(world.getTime()))));
			break;
		}
		case WINTER: {
			temperature=(int) (0.15*day*day-3.5*day+3.3-Math.random()*(-5)+Math.random()*5-Math.sqrt(Math.sqrt(Math.sqrt(world.getTime()))));
			break;
		}
		}
		return temperature;
	}
	public static int getBlockTemperature(Location loc) {
		if(!Config.enableWorlds.contains(loc.getWorld())) return 37;
		if(loc.getBlock()==null) return 37;
		World world=loc.getWorld();
		Block block=loc.getBlock(); 
		Material material=block.getType();
		if(blockTemperatures.containsKey(material)) return blockTemperatures.get(material);
		int seasonTem=TemperatureProvider.worldTemperatures.get(world);
		double tems=seasonTem;
		int tot=1;
		for(int x=-4;x<=4;x++) 
			for(int y=-4;y<=4;y++) 
				for(int z=-4;z<=4;z++) {
					Location newLoc=new Location(loc.getWorld(),loc.getX()+x,loc.getY()+y,loc.getZ()+z);
					if(newLoc.getBlock()==null) continue;
					Material blockMaterial=newLoc.getBlock().getType();
					double factor=loc.distance(newLoc);
					if(blockTemperatures.containsKey(blockMaterial)) {
						int blockTem=blockTemperatures.get(blockMaterial);
						int d_value=Math.abs(blockTem-seasonTem); 
						int influent=(int) Math.pow(d_value/factor/2.3,1.5);
						tot++;
						if(blockTem>seasonTem) tems+=seasonTem+influent;
						else tems+=seasonTem-influent;
					}
				}
		int result=(int) (tems/tot);
		result=(int) (result-(1.6*((loc.getBlockY()-50)/10)));
		return result;
	}
}
