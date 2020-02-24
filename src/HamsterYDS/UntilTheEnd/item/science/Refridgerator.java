package HamsterYDS.UntilTheEnd.item.science;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
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
import HamsterYDS.UntilTheEnd.item.basics.Gear;
import HamsterYDS.UntilTheEnd.item.materials.Brick;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Refridgerator implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.refrigerator");
	public Refridgerator() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(4,Brick.item.getType());
		recipe.addIngredient(3,Element.item.getType());
		recipe.addIngredient(2,Gear.item.getType());
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);
		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		ItemStack item4=Brick.item.clone();
		item4.setAmount(4);
		inv.setItem(14,item4);
		ItemStack item3=Element.item.clone();
		item3.setAmount(3);
		inv.setItem(15,item3);
		ItemStack item2=Gear.item.clone();
		item2.setAmount(2);
		inv.setItem(16,item2);
		UntilTheEndApi.GuideApi.addItemCraftInv("§6冰箱",inv);
		CraftGuide.addItem("§6科学",item);
		
		ItemLoader.canPlace.put("§6冰箱","Refridgerator");
	}
	
	@EventHandler public void onCraft1(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(Brick.item,4)) {
                event.setCancelled(true);
            }
            if (!event.getInventory().containsAtLeast(Element.item,3)) {
                event.setCancelled(true);
            }
            if (!event.getInventory().containsAtLeast(Gear.item,2)) {
                event.setCancelled(true);
            }
        }
	}
}
