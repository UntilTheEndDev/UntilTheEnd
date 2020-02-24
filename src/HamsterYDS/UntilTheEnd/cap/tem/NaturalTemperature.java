package HamsterYDS.UntilTheEnd.cap.tem;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.world.WorldState;
import HamsterYDS.UntilTheEnd.world.WorldState.IWorld;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class NaturalTemperature {
	public static HashMap<String,Integer> naturalTemperatures=new HashMap<String,Integer>();
	public NaturalTemperature() {
		for(World world:Bukkit.getWorlds()) {
			if(Config.disableWorlds.contains(world.getName())) continue;
			addTem(world);
		}
	}
	public static void addTem(World world) {
		naturalTemperatures.remove(world.getName());
		IWorld state=WorldState.worldStates.get(world.getName());
		int temperature=37;
		switch(state.season) {
			case SPRING: {
				temperature=(int) (Math.random()*(-5)+Math.random()*5+1.5*state.day-Math.sqrt(Math.sqrt(Math.sqrt(world.getTime()))));
				break;
			}
			case SUMMER: {
				temperature=(int) (Math.random()*(-5)+Math.random()*15-0.2*state.day*state.day+4.6*state.day+35.5-Math.sqrt(Math.sqrt(Math.sqrt(world.getTime()))));
				break;
			}
			case AUTUMN: {
				temperature=(int) (Math.random()*(-5)+Math.random()*5+50-1.5*state.day-Math.sqrt(Math.sqrt(Math.sqrt(world.getTime()))));
				break;
			}
			case WINTER: {
				temperature=(int) (0.15*state.day*state.day-3.5*state.day+3.3-Math.random()*(-5)+Math.random()*5-Math.sqrt(Math.sqrt(Math.sqrt(world.getTime()))));
				break;
			}
		}
		naturalTemperatures.put(world.getName(),temperature);
	}
}
