package HamsterYDS.UntilTheEnd.item.survival;

import org.bukkit.Bukkit;
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
import HamsterYDS.UntilTheEnd.item.basics.Ashes;
import HamsterYDS.UntilTheEnd.item.basics.Spit;
import HamsterYDS.UntilTheEnd.item.materials.Reed;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class HoneyPoultice implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.honeypaultice");
	public HoneyPoultice() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(1,Spit.item.getType());
		recipe.addIngredient(1,Reed.item.getType());
		recipe.addIngredient(7,Ashes.item.getType()); 
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		ItemStack item7=Ashes.item.clone();
		item7.setAmount(7); 
		inv.setItem(14,Spit.item);
		inv.setItem(15,Reed.item);
		inv.setItem(16,item7);
		UntilTheEndApi.GuideApi.addCraftToItem(item,inv);
		UntilTheEndApi.GuideApi.addItemToCategory("§6生存",item);
	}
	
	@EventHandler public void onCraft(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(Spit.item,1)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(Reed.item,1)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(Ashes.item,7)) {
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
			if(player.getHealth()+12.0>=player.getMaxHealth()) 
				player.setHealth(player.getMaxHealth());
			else
				player.setHealth(player.getHealth()+12.0);
		}
	}
}
