package HamsterYDS.UntilTheEnd.item.science;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.cap.hum.Humidity;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;
import HamsterYDS.UntilTheEnd.item.materials.Brick;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class LightningArrester implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.lightingarrester");
	public LightningArrester() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(4,Brick.item.getType());
		recipe.addIngredient(3,Element.item.getType());
		recipe.addIngredient(2,Material.IRON_INGOT);
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
		inv.setItem(16,new ItemStack(Material.IRON_INGOT,2));
		UntilTheEndApi.GuideApi.addCraftToItem(item,inv);
		UntilTheEndApi.GuideApi.addItemToCategory("§6科学",item);
		
		ItemLoader.canPlace.put("§6避雷针","LightningArrester");
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
        }
	}
	@EventHandler public void onLight(LightningStrikeEvent event) {
		Location loc=event.getLightning().getLocation();
		for(String str:UntilTheEndApi.BlockApi.getSpecialBlocks("LightningArrester")) {
			Location loc2=BlockApi.strToLoc(str);
			if(loc.distance(loc2)/800<=20) {
				event.setCancelled(true);
				loc2.getWorld().spawnParticle(Particle.CRIT,loc2.add(0.0,0.5,0.0),10);
				loc2.getWorld().spawnParticle(Particle.TOTEM,loc2.add(0.0,0.5,0.0),10);
			}
		}
	}
}
