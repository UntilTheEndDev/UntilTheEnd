package HamsterYDS.UntilTheEnd.cap.tem;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Influence extends BukkitRunnable implements Listener{
	public static UntilTheEnd plugin;
	public static ArrayList<String> removeBlocks=new ArrayList<String>();
	public static HashMap<String,Integer> burnTimes=new HashMap<String,Integer>();
	public Influence(UntilTheEnd plugin) {
		this.plugin=plugin;
		runTaskTimer(plugin,0L,20L);
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	@EventHandler public void onRight(PlayerInteractEvent event) {
		if(event.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
		Player player=event.getPlayer();
		if(!player.isSneaking()) return;
		Block block=event.getClickedBlock();
		Location loc=block.getLocation();
		String toString=BlockApi.locToStr(loc);
		if(burnTimes.containsKey(toString)) {
			removeBlocks.add(toString);
			player.setFireTicks(40);
			player.sendTitle("§4§l您处理了一个闷烧！","§d§l它差点就烧起来了！");
		}
	}
	public static void getBurnt(Player player) {
		if(player.getWorld().hasStorm()) return;
		Location loc=player.getLocation();
		int x=(int) (Math.random()*17-Math.random()*17);
		int y=(int) (Math.random()*17-Math.random()*17);
		int z=(int) (Math.random()*17-Math.random()*17);
		Location locNew=loc.add(x, y, z);
		int tem=NaturalTemperature.naturalTemperatures.get(player.getWorld().getName());
		if(tem>=60) 
			if(locNew.getBlock().getType().isFlammable()) 
				if(Math.random()<=0.05) {
					boolean flag=true;
					for(String str:UntilTheEndApi.BlockApi.getSpecialBlocks("IceFlingomatic")) {
						Location iceLoc=BlockApi.strToLoc(str);
						if(iceLoc.distance(locNew)<=20.0) {
							iceLoc.getWorld().spawnParticle(Particle.SNOWBALL,iceLoc.add(0.0,1.0,0.0),10);
							locNew.getWorld().spawnParticle(Particle.SNOWBALL,locNew,10);
							flag=false;
						}
					}
					if(flag) burnTimes.put(BlockApi.locToStr(locNew),(int) (20*Math.random()));
				}
		if(BlockTemperature.getTemperature(locNew)>=55) 
			if(Math.random()<=0.3) {
				boolean flag=true;
				for(String str:UntilTheEndApi.BlockApi.getSpecialBlocks("IceFlingomatic")) {
					Location iceLoc=BlockApi.strToLoc(str);
					if(iceLoc.distance(locNew)<=20.0) {
						iceLoc.getWorld().spawnParticle(Particle.SNOWBALL,iceLoc.add(0.0,1.0,0.0),10);
						locNew.getWorld().spawnParticle(Particle.SNOWBALL,locNew,10);
						flag=false;
					}
				}
				if(flag) burnTimes.put(BlockApi.locToStr(locNew),(int) (8*Math.random()));
			}
	}
	public static void getBurnt(Location loc) {
		if(loc.getWorld().hasStorm()) return;
		if(Math.random()<=0.05) 
			burnTimes.put(BlockApi.locToStr(loc),(int) (10*Math.random()));
	}
	@Override
	public void run() {
		for(Player player:Bukkit.getOnlinePlayers()) {
			if(Config.disableWorlds.contains(player.getWorld().getName())) continue;
			int tem=NaturalTemperature.naturalTemperatures.get(player.getWorld().getName());
			getBurnt(player);
			tem=PlayerManager.check(player.getName(),"tem");
			if(tem<=10) {
				player.removePotionEffect(PotionEffectType.SLOW);
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,30,0));
			}
		}
		for(String toString: removeBlocks) 
			burnTimes.remove(toString);
		for(int i=0;i<burnTimes.keySet().toArray().length;i++) {
			String toString=(String) burnTimes.keySet().toArray()[i];
			Location loc=BlockApi.strToLoc(toString);
			if(loc==null) continue;
			if(loc.getBlock().getType()==Material.AIR) {
				burnTimes.remove(toString);
				i--;
				continue;
			}
			loc.getWorld().spawnParticle(Particle.LAVA,loc,10);
			int sec=burnTimes.get(toString);
			burnTimes.remove(toString);
			if(sec<=0) {
				loc.getBlock().breakNaturally();
				loc.getBlock().setType(Material.FIRE);
			}
			burnTimes.put(toString,sec-1);
		}
	}
}
