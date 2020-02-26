package HamsterYDS.UntilTheEnd.item.basics;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class SpiderGland implements Listener{
	public static ItemStack item;
	public SpiderGland() {		
		ItemProvider.addItem(this.getClass(),item);
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
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
			if(player.getHealth()+4.0>=player.getMaxHealth()) 
				player.setHealth(player.getMaxHealth());
			else
				player.setHealth(player.getHealth()+4.0);
		}
	}
}
