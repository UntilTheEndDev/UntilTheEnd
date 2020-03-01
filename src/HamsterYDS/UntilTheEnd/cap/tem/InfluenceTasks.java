package HamsterYDS.UntilTheEnd.cap.tem;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
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
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

public class InfluenceTasks {
	public static UntilTheEnd plugin;
	public static double smoulderPercent=Temperature.yaml.getLong("smoulderPercent"); 
	public static long smoulderSpeed=Temperature.yaml.getLong("smoulderSpeed"); 
	public static int smoulderTimeout=Temperature.yaml.getInt("smoulderTimeout"); 
	public static long smoulderCancellerFireTicks=Temperature.yaml.getLong("smoulderCancellerFireTicks"); 
	public static int coldTem=Temperature.yaml.getInt("coldTem"); 
	public static int hotTem=Temperature.yaml.getInt("hotTem"); 
	public InfluenceTasks(UntilTheEnd plugin) {
		this.plugin=plugin;
		new Smoulder().runTaskTimer(plugin,0L,smoulderSpeed);
		new Damager().runTaskTimer(plugin,0L,20L);
	}
	public class Damager extends BukkitRunnable{
		@Override
		public void run() {
			for(World world:Config.enableWorlds) 
				for(Player player:world.getPlayers()) {
					int playerTem=PlayerManager.check(player.getName(),"tem");
					//TODO - LANG
					if(playerTem>hotTem) {
						player.damage(0.2*(playerTem-hotTem));
						player.sendTitle("§6§l太热了！","");
					}
					if(playerTem<coldTem) {
						player.sendTitle("§b§l太冷了！","");
						player.damage(0.2*(coldTem-playerTem));
						player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,100,100));
					}
				}
		}
	}
	public class Smoulder extends BukkitRunnable implements Listener{
		public ArrayList<String> smoulderingBlocks=new ArrayList<String>();
		@EventHandler public void onRight(PlayerInteractEvent event) {
			if(event.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
			Player player=event.getPlayer();
			Block block=event.getClickedBlock();
			Location loc=block.getLocation();
			String toString=BlockApi.locToStr(loc);
			if(smoulderingBlocks.contains(toString)) {
				smoulderingBlocks.remove(toString);
				player.setFireTicks((int) smoulderCancellerFireTicks);
				//TODO - LANG
				player.sendTitle("§4§l您处理了一个闷烧！","§d§l它差点就烧起来了！");
			}
		}
		@Override
		public void run() {
			for(World world:Config.enableWorlds) {
				for(Player player:world.getPlayers()) {
					Location playerLoc=player.getLocation();
					int x=(int) (Math.random()*17-Math.random()*17);
					int y=(int) (Math.random()*17-Math.random()*17);
					int z=(int) (Math.random()*17-Math.random()*17);
					Location loc=playerLoc.add(x, y, z);
					if(loc.getBlock()==null) continue;
					int blockTem=TemperatureProvider.getBlockTemperature(loc);
					if(blockTem>=hotTem&&Math.random()<=smoulderPercent) {
						boolean isPrevented=false;
						for(String str:BlockApi.getSpecialBlocks("IceFlingomatic")) {
							Location iceLoc=BlockApi.strToLoc(str);
							if(iceLoc.distance(loc)<=20) 
								isPrevented=true;
						}
						if(isPrevented) {
							loc.getWorld().spawnParticle(Particle.SNOWBALL,loc.add(0.0,0.5,0.0),5);
							continue;
						}
						String locStr=BlockApi.locToStr(loc);
						smoulderingBlocks.add(locStr);
						new BukkitRunnable() {
							int counter=smoulderTimeout;
							@Override
							public void run() {
								if(!smoulderingBlocks.contains(locStr)) cancel();
								counter-=20;
								loc.getWorld().spawnParticle(Particle.LAVA,loc,10);
								if(counter<=0) {
									Block block=loc.getBlock();
									block.breakNaturally();
									block.setType(Material.FIRE);
									cancel();
									smoulderingBlocks.remove(locStr);
								}
							}
						}.runTaskTimerAsynchronously(plugin,0L,20L);
					}
				}
			}
		}
	}
}
