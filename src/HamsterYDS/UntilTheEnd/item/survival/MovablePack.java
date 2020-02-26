package HamsterYDS.UntilTheEnd.item.survival;

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
import HamsterYDS.UntilTheEnd.item.basics.CowHair;
import HamsterYDS.UntilTheEnd.item.basics.RabbitFur;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class MovablePack implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.movablepack");
	public MovablePack() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(5,RabbitFur.item.getType());
		recipe.addIngredient(3,CowHair.item.getType());
		recipe.addIngredient(1,NormalPack.item.getType());
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		ItemStack item5=RabbitFur.item.clone();
		item5.setAmount(5);
		ItemStack item3=CowHair.item.clone();
		item3.setAmount(3);
		inv.setItem(14,item5);
		inv.setItem(15,NormalPack.item);
		inv.setItem(16,item3);
		UntilTheEndApi.GuideApi.addCraftToItem(item,inv);
		UntilTheEndApi.GuideApi.addItemToCategory("§6生存",item);
	}
	
	@EventHandler public void onCraft1(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(NormalPack.item,1)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(RabbitFur.item,5)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(CowHair.item,5)) {
                event.setCancelled(true);
                return;
            }
        }
	}
}
