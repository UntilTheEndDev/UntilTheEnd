package HamsterYDS.UntilTheEnd.item.materials;

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
public class Brick implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.brick");
	public Brick() {		
		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(9,Material.STONE);
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		inv.setItem(15,new ItemStack(Material.STONE,9));
		UntilTheEndApi.GuideApi.addCraftToItem(item,inv);
		UntilTheEndApi.GuideApi.addItemToCategory("§6基础",item);
	}
}
