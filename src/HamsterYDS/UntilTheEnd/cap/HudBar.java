package HamsterYDS.UntilTheEnd.cap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBarAPI;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class HudBar extends BukkitRunnable{
	public static UntilTheEnd plugin;
	public static boolean flag=false;
	public HudBar(UntilTheEnd plugin) {
		this.plugin=plugin;
		if(plugin.getConfig().getBoolean("bar.enable"))
			runTaskTimer(plugin,0L,plugin.getConfig().getLong("bar.fresh"));
	}
	@Override
	public void run() {
		for(Player player:Bukkit.getOnlinePlayers()) {
			if(Config.disableWorlds.contains(player.getWorld().getName())) continue;
			int san=PlayerManager.check(player.getName(),"san");
			int tem=PlayerManager.check(player.getName(),"tem");
			int hum=PlayerManager.check(player.getName(),"hum");
			BossBarAPI.removeAllBars(player);
			BossBarAPI.addBar(player,new TextComponent("§c§l理智   §r§b§l"+san),BossBarAPI.Color.GREEN,BossBarAPI.Style.PROGRESS,(float)((float)san/200.0),200,100); 
			BossBarAPI.addBar(player,new TextComponent("§e§l温度   §r§b§l"+tem),BossBarAPI.Color.PURPLE,BossBarAPI.Style.PROGRESS,(float)((float)tem/75.0),200,100); 
			if(player.getWorld().hasStorm()||hum>0) 
				BossBarAPI.addBar(player,new TextComponent("§3§l湿度   §r§b§l"+hum),BossBarAPI.Color.BLUE,BossBarAPI.Style.PROGRESS,(float)((float)hum/100.0),200,100); 
		}
	}
}
