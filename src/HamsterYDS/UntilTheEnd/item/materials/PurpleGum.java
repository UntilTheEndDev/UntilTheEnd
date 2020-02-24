package HamsterYDS.UntilTheEnd.item.materials;

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
import HamsterYDS.UntilTheEnd.item.basics.BlueGum;
import HamsterYDS.UntilTheEnd.item.basics.RedGum;

public class PurpleGum implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.purplegum");
	public PurpleGum() {
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(1,RedGum.item.getType());
		recipe.addIngredient(1,BlueGum.item.getType());
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		inv.setItem(15,BlueGum.item.clone());
		inv.setItem(16,RedGum.item.clone());
		UntilTheEndApi.GuideApi.addItemCraftInv("§6紫宝石",inv);
		CraftGuide.addItem("§6基础",item); 
	}
	@EventHandler public void onCraft(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(BlueGum.item,1)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(RedGum.item,1)) {
                event.setCancelled(true);
                return;
            }
        }
	}
}
