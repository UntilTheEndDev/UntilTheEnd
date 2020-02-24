package HamsterYDS.UntilTheEnd.item.clothes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.cap.hum.Humidity;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Garland implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.garland");
	public Garland() {
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(1,Material.CHORUS_FLOWER);
		recipe.addIngredient(8,Material.YELLOW_FLOWER);
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		inv.setItem(14,new ItemStack(Material.YELLOW_FLOWER,4));
		inv.setItem(15,new ItemStack(Material.CHORUS_FLOWER));
		inv.setItem(16,new ItemStack(Material.YELLOW_FLOWER,4));
		UntilTheEndApi.GuideApi.addItemCraftInv("§6花环",inv);
		CraftGuide.addItem("§6衣物",item);
	}
}
