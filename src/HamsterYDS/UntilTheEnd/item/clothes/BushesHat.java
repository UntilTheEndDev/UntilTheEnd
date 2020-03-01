package HamsterYDS.UntilTheEnd.item.clothes;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BushesHat implements Listener{
	public BushesHat() {
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6绳子"),2);
		materials.put(new ItemStack(Material.DEAD_BUSH),6);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6灌木丛帽子"),"§6衣物");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
	}
	@EventHandler public void onTarget(EntityTargetEvent event) {
		if(event.getTarget() instanceof Player) {
			Player player=(Player) event.getTarget();
			Entity entity=event.getEntity();
			ItemStack item=player.getInventory().getHelmet();
			if(item==null) return;
			if(item.getItemMeta()==null) return;
			if(item.getItemMeta().getDisplayName()==null) return;
			if(!item.getItemMeta().getDisplayName().equalsIgnoreCase("§6灌木丛帽子")) return;
			if(entity instanceof Monster) 
				if(event.getReason()==TargetReason.CLOSEST_PLAYER) 
					event.setCancelled(true);
		}
	}
}
