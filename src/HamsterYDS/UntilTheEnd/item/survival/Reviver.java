package HamsterYDS.UntilTheEnd.item.survival;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Reviver implements Listener{
	public Reviver() {	
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.items.get("Rope"),3);
		materials.put(ItemManager.items.get("SpiderGland"),3);
		ItemManager.items.get("").registerRecipe(materials,ItemManager.items.get("救赎之心"),"生存");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
	}
	@EventHandler(priority=EventPriority.HIGHEST) public void onCraft(CraftItemEvent event) {
		if(event.isCancelled()) return;
		ItemStack item=event.getRecipe().getResult().clone();
        item.setAmount(1);
        if (item.equals(ItemManager.items.get("救赎之心"))) {
        	if(event.getWhoClicked().getMaxHealth()<=2) {
        		event.getWhoClicked().sendMessage("[§cUntilTheEnd]§r 您的血量上限不足，无法制作该物品！");
        		event.setCancelled(true);
        		return;
        	}
            event.getWhoClicked().setMaxHealth(event.getWhoClicked().getMaxHealth()*0.75);
        }
	}
}
