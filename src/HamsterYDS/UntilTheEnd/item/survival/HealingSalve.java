package HamsterYDS.UntilTheEnd.item.survival;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class HealingSalve implements Listener{
	public HealingSalve() {	
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6蜘蛛腺体"),1);
		materials.put(ItemManager.namesAndItems.get("§6灰烬"),7);
		materials.put(new ItemStack(Material.BOWL),1);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6治疗药膏"),"§6生存");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
	}
	@EventHandler public void onRight(PlayerInteractEvent event) {
		Player player=event.getPlayer();
		if(event.getAction()!=Action.RIGHT_CLICK_AIR) return;
		ItemStack item=player.getItemInHand().clone();
		if(item==null) return;
		item.setAmount(1);
		if(item.equals(ItemManager.namesAndItems.get("§6治疗药膏"))) {
			event.setCancelled(true);
			if(!player.isSneaking()) return;
			ItemStack itemr=player.getItemInHand();
			itemr.setAmount(itemr.getAmount()-1);
			if(player.getHealth()+10.0>=player.getMaxHealth()) 
				player.setHealth(player.getMaxHealth());
			else
				player.setHealth(player.getHealth()+10.0);
		}
	}
}
