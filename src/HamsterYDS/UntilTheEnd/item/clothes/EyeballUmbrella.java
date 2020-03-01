package HamsterYDS.UntilTheEnd.item.clothes;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import HamsterYDS.UntilTheEnd.cap.san.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class EyeballUmbrella implements Listener{
	public EyeballUmbrella() {
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6绳子"),4);
		materials.put(ItemManager.namesAndItems.get("§6花环"),1);
		materials.put(ItemManager.namesAndItems.get("§6痰"),4);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6眼球伞"),"§6衣物");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
		
		ChangeTasks.clothesChangeSanity.put("§6眼球伞",3);
		HamsterYDS.UntilTheEnd.cap.hum.ChangeTasks.umbrellas.add("§6眼球伞");
	}
	@EventHandler public void onLight(LightningStrikeEvent event) {
		Location loc=event.getLightning().getLocation();
		for(Entity entity:loc.getWorld().getNearbyEntities(loc,10.0,10.0,10.0)) {
			if(!(entity instanceof Player)) continue;
			Player player=(Player) entity;
			ItemStack helmet=player.getInventory().getHelmet();
			if(helmet==null) continue;
			if(helmet.hasItemMeta())
				if(helmet.getItemMeta().hasDisplayName())
					if(helmet.getItemMeta().getDisplayName().equalsIgnoreCase("§6眼球伞")) {
						event.setCancelled(true);
						loc.getWorld().strikeLightning(player.getLocation().add(0.0,2.0,0.0));
						player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
						player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,600,1));
					}
		}
	}
}
