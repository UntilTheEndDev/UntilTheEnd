package HamsterYDS.UntilTheEnd.player;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.item.survival.Reviver;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class PlayerDeath extends BukkitRunnable implements Listener{
	public static UntilTheEnd plugin;
	private static ArrayList<String> deaths=new ArrayList<String>();
	private static ArrayList<String> nodeaths=new ArrayList<String>();
	public static HashMap<String,Integer> spawnSeconds=new HashMap<String,Integer>();
	public static HashMap<String,Location> spawnLocation=new HashMap<String,Location>();
	public PlayerDeath(UntilTheEnd plugin) {
		this.plugin=plugin;
		
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		runTaskTimer(plugin,0L,20L);
	}
	//普通死亡
	@EventHandler public void onDeath(PlayerDeathEvent event) {
		Player player=event.getEntity();
		if(nodeaths.contains(player.getName())) {
			event.setDeathMessage(null); 
			deaths.remove(player.getName());
			return;
		}
		deaths.remove(player.getName());
		deaths.add(player.getName());
		spawnSeconds.put(player.getName(),plugin.getConfig().getInt("player.death.autospawn"));
		spawnLocation.put(player.getName(),player.getLocation());
		event.setKeepInventory(true);
		event.setKeepLevel(true);
	}
	//自动重生
	@EventHandler public void onRespawn1(PlayerRespawnEvent event) {
		Player player=event.getPlayer();
		if(!nodeaths.contains(player.getName())) {return;}
		nodeaths.remove(player.getName());
		if(!player.hasPermission("ute.keepInventory")) {
			if(plugin.getConfig().getBoolean("player.death.clearinv"))
				player.getInventory().clear();
			if(plugin.getConfig().getBoolean("player.death.clearexp")) {
				player.setLevel(0);
				player.setExp(0);
			}
		}
	}
	//死后重生
	@EventHandler public void onRespawn2(PlayerRespawnEvent event) {
		Player player=event.getPlayer();
		if(deaths.contains(player.getName())) {
			event.setRespawnLocation(spawnLocation.get(player.getName()));
			spawnLocation.remove(player.getName());
			player.setGameMode(GameMode.SPECTATOR);
		}
	}
	@EventHandler public void onInteract(PlayerInteractEvent event) {
		Player player=event.getPlayer();
		if(deaths.contains(player.getName())) event.setCancelled(true);
		if(event.getAction()!=Action.RIGHT_CLICK_AIR) return;
		if(!player.isSneaking()) return;
		ItemStack item=player.getItemInHand().clone();
		if(item==null) return;
		item.setAmount(1);
		if(item.equals(Reviver.item)) {
			if(deaths.isEmpty()) return;
			String name=(String) deaths.toArray()[(int) (Math.random()*(deaths.toArray().length-1))];
			Player dplayer=Bukkit.getPlayer(name);
			dplayer.setGameMode(GameMode.SURVIVAL);
			dplayer.sendTitle("§6§l玩家§r§l"+player.getName()+"§6§l拯救了你","§8§l快去谢谢他吖");
			player.sendTitle("§6§l你拯救了§r§l"+name+"§6§l！","§8§lSo Kind You Are!");
			ItemStack itemr=player.getItemInHand();
			itemr.setAmount(itemr.getAmount()-1);
			PlayerManager.change(player.getName(),"san",80);
			deaths.remove(name);
			spawnSeconds.remove(name);
		}
	}
	@Override
	public void run() {
		for(int i=0;i<deaths.size();i++) {
			String name=deaths.get(i);
			Player player=Bukkit.getPlayer(name);
			if(player==null) {
				continue;
			}
			int sec=spawnSeconds.get(name);
			if(player.hasPermission("ute.quickDeath")) {
				nodeaths.add(name);
				player.setGameMode(GameMode.SURVIVAL);
				player.damage(1000000.0);
				return;
			}
			if(sec<=0) {
				nodeaths.add(name);
				player.setGameMode(GameMode.SURVIVAL);
				player.damage(1000000.0);
			}
			spawnSeconds.remove(name);
			spawnSeconds.put(name,sec-1);
			player.sendTitle("§e§l自动重生:"+sec+"秒","§8别的玩家可以来使用§d§l救赎之心§8拯救你");
		}
	}
}