package HamsterYDS.UntilTheEnd.item.materials;

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
public class Fern implements Listener{
	public static double heal=ItemManager.yaml2.getDouble("蕨类植物.heal");
	public Fern() {		
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(new ItemStack(Material.SEEDS),6);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6蕨类植物"),"§6基础");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
	}
	@EventHandler public void onRight(PlayerInteractEvent event) {
		Player player=event.getPlayer();
		if(!player.isSneaking()) return;
		if(event.getAction()!=Action.RIGHT_CLICK_AIR) return;
		ItemStack itemClone=player.getItemInHand().clone();
		if(itemClone==null) return;
		itemClone.setAmount(1);
		if(itemClone.equals(ItemManager.namesAndItems.get("§6蕨类植物"))) {
			ItemStack item=player.getItemInHand();
			item.setAmount(item.getAmount()-1);
			if(player.getHealth()+heal>=player.getMaxHealth()) player.setHealth(player.getMaxHealth());
			else player.setHealth(player.getHealth()+heal);
		}
	}
}
