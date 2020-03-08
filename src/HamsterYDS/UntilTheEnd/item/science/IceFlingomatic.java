package HamsterYDS.UntilTheEnd.item.science;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class IceFlingomatic implements Listener{
	public IceFlingomatic() {
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6齿轮"),2);
		materials.put(ItemManager.namesAndItems.get("§6电器元件"),3);
		materials.put(new ItemStack(Material.ICE),2);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6雪球发射机"),"§6科学");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
		
		ItemManager.canPlaceBlocks.put("IceFlingomatic",ItemManager.namesAndItems.get("§6雪球发射机"));
	}
	@EventHandler public void onBurn(BlockIgniteEvent event) {
		if(event.isCancelled()) return;
		for(String str:UntilTheEndApi.BlockApi.getSpecialBlocks("IceFlingomatic")) {
			Location loc=BlockApi.strToLoc(str);
			Location loc2=event.getBlock().getLocation();
			if(loc.distance(loc2)<=40) {
				loc2.getWorld().spawnParticle(Particle.SNOWBALL,loc2,50);
				loc.getWorld().spawnParticle(Particle.SNOWBALL,loc.add(0.5,1.0,0.5),50);
				event.setCancelled(true);
				return;
			}
		}
	}
}
