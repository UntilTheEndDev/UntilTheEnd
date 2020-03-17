package HamsterYDS.UntilTheEnd.item.science;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class LightningArrester implements Listener{
	public LightningArrester() {
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.items.get("Brick"),4);
		materials.put(ItemManager.items.get("电器元件"),3);
		materials.put(new ItemStack(Material.IRON_INGOT),2);
		ItemManager.items.get("").registerRecipe(materials,ItemManager.items.get("避雷针"),"科学");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
		
		ItemManager.canPlaceBlocks.put("LightningArrester",ItemManager.items.get("避雷针"));
	}
	@EventHandler public void onLight(LightningStrikeEvent event) {
		Location loc=event.getLightning().getLocation();
		for(String str:UntilTheEndApi.BlockApi.getSpecialBlocks("LightningArrester")) {
			Location loc2=BlockApi.strToLoc(str);
			if(loc.distance(loc2)<=20) {
				event.setCancelled(true);
				loc.getWorld().strikeLightning(loc2.add(0.5,0.5,0.5));
				loc2.getWorld().spawnParticle(Particle.CRIT,loc2.add(0.0,0.5,0.0),10);
				loc2.getWorld().spawnParticle(Particle.TOTEM,loc2.add(0.0,0.5,0.0),10);
			}
		}
	}
}
