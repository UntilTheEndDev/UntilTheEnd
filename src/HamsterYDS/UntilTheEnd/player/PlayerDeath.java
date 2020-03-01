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
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class PlayerDeath extends BukkitRunnable implements Listener{
	public static UntilTheEnd plugin;
	public static ArrayList<String> souls=new ArrayList<String>(); 
	public static HashMap<String,Integer> spawnSeconds=new HashMap<String,Integer>();
	public static HashMap<String,Location> spawnLocations=new HashMap<String,Location>();
	public PlayerDeath(UntilTheEnd plugin) {
		this.plugin=plugin;
		if(!plugin.getConfig().getBoolean("player.death.enable")) return;
		runTaskTimer(plugin,0L,20L);
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	//普通死亡
	@EventHandler public void onDeath(PlayerDeathEvent event) {
		Player player=event.getEntity();
		spawnSeconds.put(player.getName(),plugin.getConfig().getInt("player.death.autorespawn"));
		spawnLocations.put(player.getName(),player.getLocation());
		player.spigot().respawn();
		player.teleport(spawnLocations.get(player.getName()));
		player.setGameMode(GameMode.SPECTATOR);
	}
	@EventHandler public void onInteract(PlayerInteractEvent event) {
		Player player=event.getPlayer();
		if(event.getAction()!=Action.RIGHT_CLICK_AIR) return;
		if(!player.isSneaking()) return;
		ItemStack item=player.getItemInHand().clone();
		if(item==null) return;
		item.setAmount(1);
		if(item.equals(ItemManager.namesAndItems.get("§6救赎之心"))) {
			if(souls.isEmpty()) return;
			String name=(String) souls.toArray()[(int) (Math.random()*(souls.toArray().length-1))];
			Player dplayer=Bukkit.getPlayer(name);
			if(dplayer==null) return;
			dplayer.teleport(spawnLocations.get(dplayer.getName()));
			dplayer.setGameMode(GameMode.SURVIVAL);
			dplayer.sendTitle("§6§l玩家§r§l"+player.getName()+"§6§l拯救了你","§8§l快去谢谢他吖");
			player.sendTitle("§6§l你拯救了§r§l"+name+"§6§l！","§8§lSo Kind You Are!");
			ItemStack itemr=player.getItemInHand();
			itemr.setAmount(itemr.getAmount()-1);
			PlayerManager.change(player.getName(),"san",80);
			souls.remove(name);
			spawnSeconds.remove(name);
			spawnLocations.remove(name);
		}
	}
	@Override
	public void run() {
		for(int i=0;i<souls.size();i++) {
			String name=souls.get(i);
			Player player=Bukkit.getPlayer(name);
			if(player==null) {
				souls.remove(name);
				spawnSeconds.remove(name);
				spawnLocations.remove(name);
				continue;
			}
			int sec=spawnSeconds.get(name);
			if(sec<=0) {
				souls.remove(name);
				spawnSeconds.remove(name);
				spawnLocations.remove(name);
				if(plugin.getConfig().getBoolean("player.death.clearinv")) {
					player.getInventory().clear();
				}
				if(plugin.getConfig().getBoolean("player.death.clearexp")) {
					player.setLevel(0);
					player.setExp(0); 
				}
				player.spigot().respawn();
				player.setGameMode(GameMode.SURVIVAL);
			}
			spawnSeconds.remove(name);
			spawnSeconds.put(name,sec-1);
			player.sendMessage("§e§l自动重生:"+sec+"秒");
		}
	}
}