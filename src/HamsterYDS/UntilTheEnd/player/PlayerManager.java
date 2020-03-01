package HamsterYDS.UntilTheEnd.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.cap.HudBar;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class PlayerManager implements Listener{
	public static UntilTheEnd plugin;
	private static HashMap<String,IPlayer> players=new HashMap<String,IPlayer>();
	public PlayerManager() {}
	public PlayerManager(UntilTheEnd plugin) {
		this.plugin=plugin;
		new SavingTask();
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	@EventHandler public void onJoin(PlayerJoinEvent event) {
		Player player=event.getPlayer();
		load(player.getName());
	}
	@EventHandler public void onQuit(PlayerQuitEvent event) {
		Player player=event.getPlayer();
		save(player.getName());
		players.remove(player.getName());
	}
	@EventHandler public void onDeath(PlayerDeathEvent event) {
		players.remove(event.getEntity().getName());
		IPlayer player=new PlayerManager().new IPlayer(37,0,200);
		players.put(event.getEntity().getName(),player);
	}
	public static void load(String name) {
		File file=new File(plugin.getDataFolder()+"/playerdata/",name+".yml");
		if(!file.exists()) {
			IPlayer player=new PlayerManager().new IPlayer(37,0,200);
			players.put(name,player);
			return;
		}
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder()+"/playerdata/",name+".yml"));
		int humidity=yaml.getInt(name+".humidity");
		int temperature=yaml.getInt(name+".temperature");
		int sanity=yaml.getInt(name+".sanity");
		if(!yaml.getKeys(false).contains(name)) {
			humidity=0;
			temperature=37;
			sanity=200;
		}
		IPlayer player=new PlayerManager().new IPlayer(temperature, humidity, sanity);
		players.put(name,player);
	}
	public static void save(String name) {
		IPlayer player=players.get(name);
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder()+"/playerdata/",name+".yml"));
		yaml.set(name+".humidity",player.humidity);
		yaml.set(name+".temperature",player.temperature);
		yaml.set(name+".sanity",player.sanity);
		try {
			yaml.save(new File(plugin.getDataFolder()+"/playerdata/",name+".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static int check(String name,String type) {
		IPlayer player=players.get(name);
		switch(type){
			case "tem": return player.temperature;
			case "hum": return player.humidity;
			case "san": return player.sanity;
			default: return -1;
		}
	}
	public static void change(String name,String type,int changement) {
		Player rplayer=Bukkit.getPlayer(name);
		if(rplayer==null) 
			return;
		if(rplayer.getGameMode()==GameMode.CREATIVE||rplayer.getGameMode()==GameMode.SPECTATOR) 
			return;
		IPlayer player=players.get(name);
		String mark="";
		if(changement>0) mark="↑";
		if(changement<0) mark="↓";
		if(changement==0) mark=" ";
		switch(type){
			case "tem": {
				player.temperature+=changement;
				HudBar.temperature.remove(name);
				HudBar.temperature.put(name,mark);
				if(player.temperature==5) rplayer.sendTitle("§9太冷了！","");
				if(player.temperature==60) rplayer.sendTitle("§9太热了！","");
				if(player.temperature<-5) player.temperature=-5;
				if(player.temperature>75) player.temperature=75;
				break;
			}
			case "hum": {
				player.humidity+=changement;
				HudBar.humidity.remove(name);
				HudBar.humidity.put(name,mark);
				if(player.humidity<0) player.humidity=0;
				if(player.humidity>100) player.humidity=100;
				break;
			}
			case "san": {
				player.sanity+=changement;
				HudBar.sanity.remove(name);
				HudBar.sanity.put(name,mark);
				if(player.sanity<0) player.sanity=0;
				if(player.sanity>200) player.sanity=200;
				break;
			}
		}
		players.remove(name);
		players.put(name,player);
	}
	private class SavingTask extends BukkitRunnable{
		@Override
		public void run() {
			for(Player player:Bukkit.getOnlinePlayers()) 
				save(player.getName());
		}
		public SavingTask() {
			runTaskTimer(plugin,0L,plugin.getConfig().getInt("player.stats.autosave")*20);
		}
	}
	private class IPlayer {
		public int temperature;
		public int humidity;
		public int sanity;
		public IPlayer(int temperature,int humidity,int sanity) {
			this.temperature=temperature;
			this.humidity=humidity;
			this.sanity=sanity;
		}
	}
}
