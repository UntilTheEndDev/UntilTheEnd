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
import HamsterYDS.UntilTheEnd.item.basics.PigSkin;
import HamsterYDS.UntilTheEnd.item.materials.Reed;
import HamsterYDS.UntilTheEnd.item.materials.Rope;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Umbrella implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.umbrella");
	public Umbrella() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(4,Rope.item.getType());
		recipe.addIngredient(2,Reed.item.getType());
		recipe.addIngredient(2,PigSkin.item.getType());
		recipe.addIngredient(1,Material.STRING);
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		ItemStack item4=Rope.item.clone();
		item4.setAmount(4);
		ItemStack item2_1=Reed.item.clone();
		item2_1.setAmount(2);
		ItemStack item2_2=PigSkin.item.clone();
		item2_2.setAmount(2);
		inv.setItem(13,item4);
		inv.setItem(14,item2_1);
		inv.setItem(15,item2_2);
		inv.setItem(16,new ItemStack(Material.STRING));
		UntilTheEndApi.GuideApi.addCraftToItem(item,inv);
		UntilTheEndApi.GuideApi.addItemToCategory("§6生存",item);
	}
	
	@EventHandler public void onCraft(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(Rope.item,4)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(Reed.item,2)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(PigSkin.item,1)) {
                event.setCancelled(true);
                return;
            }
        }
	}
}
