package HamsterYDS.UntilTheEnd.item.clothes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.cap.hum.Humidity;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;
import HamsterYDS.UntilTheEnd.item.basics.Spit;
import HamsterYDS.UntilTheEnd.item.materials.Rope;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class EyeballUmbrella implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.eyeballumbrella");
	public EyeballUmbrella() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(4,Spit.item.getType());
		recipe.addIngredient(1,Garland.item.getType());
		recipe.addIngredient(4,Rope.item.getType());
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		ItemStack item4_1=Spit.item.clone();
		item4_1.setAmount(4); 
		ItemStack item4_2=Rope.item.clone();
		item4_2.setAmount(4); 
		inv.setItem(14,item4_1);
		inv.setItem(15,Garland.item);
		inv.setItem(16,item4_2);
		UntilTheEndApi.GuideApi.addItemCraftInv("§6眼球伞",inv);
		CraftGuide.addItem("§6衣物",item);
	}
	
	@EventHandler public void onCraft(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(Spit.item,4)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(Rope.item,4)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(Garland.item,1)) {
                event.setCancelled(true);
                return;
            }
        }
	}
	@EventHandler public void onLight(LightningStrikeEvent event) {
		Location loc=event.getLightning().getLocation();
		for(Entity entity:loc.getWorld().getNearbyEntities(loc,10.0,10.0,10.0)) {
			if(!(entity instanceof Player)) continue;
			Player player=(Player) entity;
			ItemStack helmet=player.getInventory().getHelmet();
			if(helmet==null) continue;
			if(helmet.getItemMeta()==null) continue;
			if(item.getItemMeta().getDisplayName()==null) continue;
			if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§6眼球伞")) {
				event.getLightning().teleport(player.getLocation().add(0.0,2.0,0.0));
				player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,600,1));
			}
		}
	}
}
