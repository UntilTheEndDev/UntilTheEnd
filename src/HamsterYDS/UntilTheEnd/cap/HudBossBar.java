package HamsterYDS.UntilTheEnd.cap;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠 
 * @version V5.1.1
 */
public class HudBossBar extends BukkitRunnable{
	public HashMap<String,BossBar> bossBars=new HashMap<String,BossBar>();
	public HashMap<String,BossBar> bossBart=new HashMap<String,BossBar>();
	public HashMap<String,BossBar> bossBarh=new HashMap<String,BossBar>();
	@Override
	public void run() {
		for(World world:Config.enableWorlds)
			for(Player player:world.getPlayers()) { 
				int san=PlayerManager.check(player.getName(),"san");
				int tem=PlayerManager.check(player.getName(),"tem");
				int hum=PlayerManager.check(player.getName(),"hum");
				BossBar bars=(!bossBars.containsKey(player.getName()))?Bukkit.createBossBar("",BarColor.GREEN,BarStyle.SOLID,BarFlag.CREATE_FOG):bossBars.get(player.getName());
				bars.setTitle("§6§l"+HudProvider.sanity.get(player.getName())+"  §c§l理智   §r§b§l"+san+"  §6§l"+HudProvider.sanity.get(player.getName()));
				bars.setProgress(((float)san/200.0));
				bars.addPlayer(player);
				
				BossBar bart=(!bossBart.containsKey(player.getName()))?Bukkit.createBossBar("",BarColor.PURPLE,BarStyle.SOLID,BarFlag.CREATE_FOG):bossBart.get(player.getName());
				bart.setTitle("§6§l"+HudProvider.temperature.get(player.getName())+"  §e§l温度   §r§b§l"+tem+"  §6§l"+HudProvider.temperature.get(player.getName()));
				bart.setProgress((((float)tem+5)/85.0));
				bart.addPlayer(player);
				
				BossBar barh=(!bossBarh.containsKey(player.getName()))?Bukkit.createBossBar("",BarColor.BLUE,BarStyle.SOLID,BarFlag.CREATE_FOG):bossBarh.get(player.getName());
				barh.setTitle("§6§l"+HudProvider.humidity.get(player.getName())+"  §3§l湿度   §r§b§l"+hum+"  §6§l"+HudProvider.humidity.get(player.getName()));
				barh.setProgress(((float)hum/100.0));
				if((hum!=0||world.hasStorm())&&(player.getGameMode()!=GameMode.CREATIVE&&player.getGameMode()!=GameMode.SPECTATOR)) 
					barh.addPlayer(player);
				bossBars.remove(player.getName());
				bossBart.remove(player.getName());
				bossBarh.remove(player.getName());
				bossBars.put(player.getName(),bars);
				bossBart.put(player.getName(),bart);
				bossBarh.put(player.getName(),barh);
			}
	}
}
