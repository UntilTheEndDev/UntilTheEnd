package HamsterYDS.UntilTheEnd.world;

import java.util.HashSet;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.cap.tem.TemperatureProvider;
import HamsterYDS.UntilTheEnd.world.WorldProvider.IWorld;
import HamsterYDS.UntilTheEnd.world.WorldProvider.Season;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class WorldCounter extends BukkitRunnable{
	//TODO
	static int spring=HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getInt("world.season.spring");
	static int autumn=HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getInt("world.season.autumn");
	static int summer=HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getInt("world.season.summer");
	static int winter=HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getInt("world.season.winter");
	private HashSet<String> changingWorlds=new HashSet<String>();
	@Override
	public void run() {
		for(World world:Config.enableWorlds) {
			long time=world.getTime();
			if(time>=23970) changingWorlds.add(world.getName());
			if(time>=0&&time<=50) 
				if(changingWorlds.contains(world.getName())) {
					changingWorlds.remove(world.getName());
					newDay(world);
				}
		}
	}
	private void newDay(World world) {
		IWorld state=WorldProvider.worldStates.get(world.getName());
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
		WorldProvider.worldStates.remove(world.getName());
		WorldProvider.worldStates.put(world.getName(),state);
		WorldProvider.saveWorlds();
		tellPlayers(world);
		TemperatureProvider.loadWorldTemperature(world);
	}
	public static void tellPlayers(World world) {
		IWorld state=WorldProvider.worldStates.get(world.getName());
		for(Player player:world.getPlayers()) 
			player.sendTitle("§e§l"+state.season.name,"§c第§d§l"+state.day+"§r§c天", 10, 70, 20);//TODO-lang
	}
}
