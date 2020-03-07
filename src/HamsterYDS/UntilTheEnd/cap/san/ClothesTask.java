package HamsterYDS.UntilTheEnd.cap.san;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.cap.clothes.GetSan;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class ClothesTask extends BukkitRunnable{
	public static UntilTheEnd plugin;
	public ClothesTask(UntilTheEnd plugin) {
		this.plugin=plugin;
		runTaskTimer(plugin,0L,100L); 
	}
	@Override
	public void run() {
		for(World world:Bukkit.getWorlds()) {
			if(!Config.enableWorlds.contains(world)) continue;
			for(Player player:world.getPlayers()) {
				int fac=GetSan.getSan(player);
				PlayerManager.change(player.getName(),"san",fac);
			}
		}
	}
}
