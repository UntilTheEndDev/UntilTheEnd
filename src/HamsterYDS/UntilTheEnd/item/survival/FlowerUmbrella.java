package HamsterYDS.UntilTheEnd.item.survival;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import HamsterYDS.UntilTheEnd.item.basics.CatTail;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class FlowerUmbrella implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.flowerumbrella");
	public FlowerUmbrella() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(1,Umbrella.item.getType());
		recipe.addIngredient(1,CatTail.item.getType());
		recipe.addIngredient(2,Material.CHORUS_FLOWER);
		recipe.addIngredient(2,Material.YELLOW_FLOWER);
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		inv.setItem(13,Umbrella.item);
		inv.setItem(14,CatTail.item);
		inv.setItem(15,new ItemStack(Material.CHORUS_FLOWER,2));
		inv.setItem(16,new ItemStack(Material.YELLOW_FLOWER,2));
		UntilTheEndApi.GuideApi.addItemCraftInv("§6花伞",inv);
		CraftGuide.addItem("§6生存",item);
	}
	
	@EventHandler public void onCraft(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(Umbrella.item,1)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(CatTail.item,1)) {
                event.setCancelled(true);
                return;
            }
        }
	}
}
