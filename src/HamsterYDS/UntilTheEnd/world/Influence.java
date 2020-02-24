package HamsterYDS.UntilTheEnd.world;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Influence extends BukkitRunnable implements Listener{
	private static HashMap<String,Integer> darkness=new HashMap<String,Integer>();
	public static UntilTheEnd plugin;
	static int warn=0;
	static int attack=0;
	static int damage=0;
	static int san_warn=0;
	static int san_attack=0;
	public Influence(UntilTheEnd plugin) {
		this.plugin=plugin;
		warn=plugin.getConfig().getInt("world.darkness.warn");
		attack=plugin.getConfig().getInt("world.darkness.attack");
		damage=plugin.getConfig().getInt("world.darkness.damage");
		san_warn=plugin.getConfig().getInt("world.darkness.san_warn");
		san_attack=plugin.getConfig().getInt("world.darkness.san_attack");
		runTaskTimer(plugin,0L,20L);
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	@Override
	public void run() {
		for(World world:Bukkit.getWorlds()) {
			if(Config.disableWorlds.contains(world.getName())) continue;
			for(Player player:world.getPlayers()) {
				if((player.getLocation().getBlock().getLightFromBlocks()==0&&
						player.getWorld().getTime()<=WorldTime.down&&
						player.getWorld().getTime()>=WorldTime.up)||player.getLocation().getBlock().getLightLevel()==0) {
					if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) continue;
					if(darkness.containsKey(player.getName())) {
						int second=darkness.remove(player.getName());
						darkness.remove(player.getName());
						darkness.put(player.getName(),second+1);
					}else darkness.put(player.getName(),1);
				} else darkness.remove(player.getName());
				if(darkness.containsKey(player.getName())) {
					if(darkness.get(player.getName())==warn) {
						player.sendTitle("§8我什么也看不见....","§8那是什么声音？");
						PlayerManager.change(player.getName(),"san",san_warn);
					}
					if(darkness.get(player.getName())>=attack) {
						player.sendTitle("§8ヾ(≧O≦)〃啊~！什么东西？","§8他打了我一下....");
						player.damage(damage);
						PlayerManager.change(player.getName(),"san",san_attack);
					}
				}
			}	
		}
	}
}
