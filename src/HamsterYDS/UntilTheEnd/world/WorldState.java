package HamsterYDS.UntilTheEnd.world;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.cap.tem.NaturalTemperature;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class WorldState{
	public static UntilTheEnd plugin;
	public static HashMap<String,IWorld> worldStates=new HashMap<String,IWorld>();
	static int spring=0;
	static int autumn=0;
	static int summer=0;
	static int winter=0;
	public WorldState() {}
	public WorldState(UntilTheEnd plugin) {
		this.plugin=plugin;
		spring=plugin.getConfig().getInt("world.season.spring");
		autumn=plugin.getConfig().getInt("world.season.autumn");
		summer=plugin.getConfig().getInt("world.season.summer");
		winter=plugin.getConfig().getInt("world.season.winter");
		File file=new File(plugin.getDataFolder(),"worlds.yml");
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		for(World world:Bukkit.getWorlds()) {
			if(Config.disableWorlds.contains(world.getName())) continue;
			String worldName=world.getName();
			if(!yaml.getKeys(false).contains(worldName)) {
				worldStates.put(worldName,new IWorld(Season.AUTUMN,1));
				yaml.set(worldName+".season",Season.AUTUMN.name);
				yaml.set(worldName+".day",1);
				try {
					yaml.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				Season season=Season.getSeason(yaml.getString(worldName+".season"));
				int day=yaml.getInt(worldName+".day");
				worldStates.put(worldName,new IWorld(season,day));
			}
		}
		new DayCounter();
	}
	private static void save(World world) {
		File file=new File(plugin.getDataFolder(),"worlds.yml");
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		IWorld state=worldStates.get(world.getName());
		yaml.set(world.getName()+".season",state.season.name);
		yaml.set(world.getName()+".day",state.day);
		try {
			yaml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static class DayCounter extends BukkitRunnable{
		public HashMap<String,Boolean> worldFlags=new HashMap<String,Boolean>();
		public DayCounter() {
			runTaskTimer(plugin,0L,20L);
		}
		@Override
		public void run() {
			for(World world:Bukkit.getWorlds()) {
				if(Config.disableWorlds.contains(world.getName())) continue;
				long time=world.getTime();
				if(time>=23950) {
					worldFlags.remove(world.getName());
					worldFlags.put(world.getName(),true);
				}
				if(time>=0&&time<=100) {
					if(worldFlags.containsKey(world.getName())) 
						if(worldFlags.get(world.getName())) {
							newDay(world);
							worldFlags.remove(world.getName());
							worldFlags.put(world.getName(),false);
						}
				}
			}
		}
		private void newDay(World world) {
			IWorld state=worldStates.get(world.getName());
			worldStates.remove(world.getName());
			int days=state.day;
			switch(state.season) {
			case SPRING: {
				if(days>=spring+Math.random()*10-Math.random()*10) {
					state.day=1;
					state.season=Season.SUMMER;
				}else state.day++;
				break;
			}
			case SUMMER: {
				if(days>=summer+Math.random()*5-Math.random()*5) {
					state.day=1;
					state.season=Season.AUTUMN;
				}else state.day++;
				break;
			}
			case AUTUMN: {
				if(days>=autumn+Math.random()*10-Math.random()*10) {
					state.day=1;
					state.season=Season.WINTER;
				}else state.day++;
				break;
			}
			case WINTER: {
				if(days>=winter+Math.random()*5-Math.random()*5) {
					state.day=1;
					state.season=Season.SPRING;
				}else state.day++;
				break;
			}
			default: break;
			}
			worldStates.put(world.getName(),state);
			save(world);
			tellPlayers(world);
			NaturalTemperature.addTem(world);
		}
		public static void tellPlayers(World world) {
			IWorld state=worldStates.get(world.getName());
			for(Player player:world.getPlayers()) {
				player.sendTitle("§e§l"+state.season.name,"§c第§d§l"+state.day+"§r§c天", 10, 70, 20);
			}
		}
	}
	public class IWorld{
		public Season season;
		public int day;
		public IWorld(Season season,int day) {
			this.season=season;
			this.day=day;
		}
	}
	public enum Season{
		SPRING("春天"),SUMMER("夏天"),AUTUMN("秋天"),WINTER("冬天");
		String name;
		Season(String name) {this.name=name;}
		static Season getSeason(String name){
			switch(name) {
			case "春天": return SPRING;
			case "夏天": return SUMMER;
			case "秋天": return AUTUMN;
			case "冬天": return WINTER;
			default: return AUTUMN;
			}
		}
	}
}
