package HamsterYDS.UntilTheEnd.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class WorldTime extends BukkitRunnable implements Listener{
	public static UntilTheEnd plugin;
	static long up=0;
	static long down=0;
	public WorldTime(UntilTheEnd plugin) {
		this.plugin=plugin;
		up=plugin.getConfig().getLong("world.blind.up");
		down=plugin.getConfig().getLong("world.blind.down");
		runTaskTimer(plugin,0L,40L);
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	@Override
	public void run() {
		for(World world:Bukkit.getWorlds()) {
			if(Config.disableWorlds.contains(world.getName())) continue;
			long time=world.getTime();
			if(time>=up&&time<=down) 
				for(Player player:world.getPlayers()) {
					if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) continue;
					player.removePotionEffect(PotionEffectType.BLINDNESS);
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,100,0));
				}
		}
	}
}
