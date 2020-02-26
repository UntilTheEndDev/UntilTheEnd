package HamsterYDS.UntilTheEnd.item.basics;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Ashes implements Listener{
	public static ItemStack item;
	public Ashes() {		
		ItemProvider.addItem(this.getClass(),item);
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
	}
	@EventHandler(priority=EventPriority.HIGHEST) public void onBurn(BlockBurnEvent event) {
		if(event.isCancelled()) return;
		if(Math.random()<=0.25) {
			event.getBlock().getWorld().dropItem(event.getBlock().getLocation(),item.clone());
		}
	}
}
