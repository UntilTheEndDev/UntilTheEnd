package HamsterYDS.UntilTheEnd.item.survival;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
public class ACDDrug implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.acddrug");
	public ACDDrug() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(4,HoneyPoultice.item.getType());
		recipe.addIngredient(1,Material.BOWL);
		recipe.addIngredient(4,HealingSalve.item.getType());
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		ItemStack item4_1=HoneyPoultice.item.clone();
		item4_1.setAmount(4); 
		ItemStack item4_2=HealingSalve.item.clone();
		item4_2.setAmount(4); 
		inv.setItem(14,item4_1);
		inv.setItem(15,new ItemStack(Material.BOWL));
		inv.setItem(16,item4_2);
		UntilTheEndApi.GuideApi.addCraftToItem(item,inv);
		UntilTheEndApi.GuideApi.addItemToCategory("§6生存",item);
	}
	
	@EventHandler public void onCraft(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(HoneyPoultice.item,1)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(HealingSalve.item,1)) {
                event.setCancelled(true);
                return;
            }
        }
	}
	@EventHandler public void onRight(PlayerInteractEvent event) {
		Player player=event.getPlayer();
		if(!player.isSneaking()) return;
		if(event.getAction()!=Action.RIGHT_CLICK_AIR) return;
		ItemStack item=player.getItemInHand().clone();
		if(item==null) return;
		item.setAmount(1);
		if(item.equals(this.item)) {
			ItemStack itemr=player.getItemInHand();
			itemr.setAmount(itemr.getAmount()-1);
			if(player.getMaxHealth()+6.0>=40.0) 
				player.setMaxHealth(40.0);
			else
				player.setMaxHealth(player.getMaxHealth()+6.0);
		}
	}
}
