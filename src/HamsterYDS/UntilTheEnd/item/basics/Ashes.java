package HamsterYDS.UntilTheEnd.item.basics;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Ashes implements Listener {
	public static double percent = ItemManager.itemAttributes.getDouble("Ashes.percent");

	public Ashes() {
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBurn(BlockBurnEvent event) {
		if (event.isCancelled())
			return;
		if (Math.random() <= percent) {
			event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), ItemManager.items.get("Ashes").item);
		}
	}
}
