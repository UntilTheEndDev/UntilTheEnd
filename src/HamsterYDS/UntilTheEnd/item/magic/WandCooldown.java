package HamsterYDS.UntilTheEnd.item.magic;

import java.util.HashMap;

import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.item.ItemLoader;

public class WandCooldown extends BukkitRunnable{
	public static HashMap<String,Integer> cd=new HashMap<String,Integer>();
	public WandCooldown() {
		runTaskTimer(ItemLoader.plugin,0L,20L);
	}
	@Override
	public void run() {
		for(int index=0;index<cd.keySet().toArray().length;index++) {
			String string=(String) cd.keySet().toArray()[index];
			int sec=cd.get(string);
			cd.remove(string);
			cd.put(string,sec);
		}
	}
}
