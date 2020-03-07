package HamsterYDS.UntilTheEnd.item.combat;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import HamsterYDS.UntilTheEnd.item.ItemManager;

public class WeatherPain implements Listener{
	public static int dist=ItemManager.yaml2.getInt("旋风.dist");
	public static double range=ItemManager.yaml2.getDouble("旋风.range");
	public WeatherPain() {
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6齿轮"),3);
		materials.put(ItemManager.namesAndItems.get("§6牛角"),2);
		materials.put(ItemManager.namesAndItems.get("§6牛毛"),4);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6旋风"),"§6战斗");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
	}
	private static HashMap<String,Integer> cd=new HashMap<String,Integer>();
	@EventHandler public void onRight(PlayerInteractEvent event) {
		Player player=event.getPlayer();
		if(!(event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK)) return;
		ItemStack itemClone=player.getItemInHand().clone();
		if(itemClone==null) return;
		itemClone.setAmount(1);
		itemClone.setDurability((short) 0);
		if(itemClone.equals(ItemManager.namesAndItems.get("§6旋风"))) {
			if(cd.containsKey(player.getName()))
				if(cd.get(player.getName())>0) {
					player.sendMessage("§6[§cUntilTheEnd§6]§r 旋风使用冷却时间未到！"); 
					return;
				}
			cd.remove(player.getName());
			cd.put(player.getName(),10);
			ItemStack itemr=player.getItemInHand();
			itemr.setDurability((short) (itemr.getDurability()+25));
			if(itemr.getDurability()>itemr.getType().getMaxDurability()) 
				player.setItemInHand(null);
			Location loc=player.getLocation().add(0.0,1.0,0.0);
			Vector vec=player.getEyeLocation().getDirection().multiply(0.5);
			new BukkitRunnable() {
				int range=150;
				@Override
				public void run() {
					for(int i=0;i<5;i++) {
						range--;
						if(range<=0) {
							cancel();
							cd.remove(player.getName());
							return;
						}
						loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,loc,1);
						loc.add(vec);
						for(Entity entity:loc.getWorld().getNearbyEntities(loc,WeatherPain.range,WeatherPain.range,WeatherPain.range)) {
							if(entity.getEntityId()==player.getEntityId()) continue;
							for(int j=0;j<=dist;j++) entity.setVelocity(vec);
						}
					}
				}
			}.runTaskTimer(ItemManager.plugin,0L,3L);
			event.setCancelled(true);
		}
	}
}
