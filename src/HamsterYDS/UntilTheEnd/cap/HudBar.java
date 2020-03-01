package HamsterYDS.UntilTheEnd.cap;

import java.util.HashMap;

import org.bukkit.World;
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
	public static HashMap<String,String> sanity=new HashMap<String,String>();
	public static HashMap<String,String> humidity=new HashMap<String,String>();
	public static HashMap<String,String> temperature=new HashMap<String,String>();
	@Override
	public void run() {
		for(World world:Config.enableWorlds)
			for(Player player:world.getPlayers()) {
				int san=PlayerManager.check(player.getName(),"san");
				int tem=PlayerManager.check(player.getName(),"tem");
				int hum=PlayerManager.check(player.getName(),"hum");
				BossBarAPI.removeAllBars(player);
				BossBarAPI.addBar(player,new TextComponent("§l§6"+sanity.get(player.getName())+"  §c§l理智   §r§b§l"+san+"  §l§6"+sanity.get(player.getName())),BossBarAPI.Color.GREEN,BossBarAPI.Style.PROGRESS,(float)((float)san/200.0),200,100); 
				BossBarAPI.addBar(player,new TextComponent("§l§6"+temperature.get(player.getName())+"  §e§l温度   §r§b§l"+tem+"  §l§6"+temperature.get(player.getName())),BossBarAPI.Color.PURPLE,BossBarAPI.Style.PROGRESS,(float)((float)tem/75.0),200,100); 
				if(player.getWorld().hasStorm()||hum>0) 
					BossBarAPI.addBar(player,new TextComponent("§l§6"+humidity.get(player.getName())+"  §3§l湿度   §r§b§l"+hum+"  §l§6"+humidity.get(player.getName())),BossBarAPI.Color.BLUE,BossBarAPI.Style.PROGRESS,(float)((float)hum/100.0),200,100); 
			}
	}
}
