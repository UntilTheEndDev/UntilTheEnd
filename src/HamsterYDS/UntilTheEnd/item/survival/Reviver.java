package HamsterYDS.UntilTheEnd.item.survival;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.cap.hum.Humidity;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;
import HamsterYDS.UntilTheEnd.item.basics.SpiderGland;
import HamsterYDS.UntilTheEnd.item.materials.Rope;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Reviver implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.reviver");
	public Reviver() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(3,Rope.item.getType());
		recipe.addIngredient(3,SpiderGland.item.getType());
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		
		ItemStack item3=Rope.item.clone();
		item3.setAmount(3);
		
		ItemStack item3_2=SpiderGland.item.clone();
		item3_2.setAmount(3);
		
		inv.setItem(14,item3);
		inv.setItem(15,item3_2);
		UntilTheEndApi.GuideApi.addCraftToItem(item,inv);
		UntilTheEndApi.GuideApi.addItemToCategory("§6生存",item);
	}
	
	@EventHandler public void onCraft(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(Rope.item,3)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(SpiderGland.item,3)) {
                event.setCancelled(true);
                return;
            }
        }
	}
	@EventHandler(priority=EventPriority.HIGHEST) public void onCraft2(CraftItemEvent event) {
		if(event.isCancelled()) return;
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
        	if(event.getWhoClicked().getMaxHealth()<=2) {
        		event.getWhoClicked().sendMessage("§6[§c凌域§6]§r 您的血量上限不足，无法制作该物品！");
        		event.setCancelled(true);
        		return;
        	}
            event.getWhoClicked().setMaxHealth(event.getWhoClicked().getMaxHealth()*0.75);
        }
	}
}
