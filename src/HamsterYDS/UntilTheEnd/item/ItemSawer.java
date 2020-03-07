package HamsterYDS.UntilTheEnd.item;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class ItemSawer extends BukkitRunnable {
	@Override
	public void run() {
		for (World world : Config.enableWorlds) {
			for (Entity entity : world.getEntities()) {
				if (!(entity instanceof Item))
					continue;
				Item item = (Item) entity;
				if (item.getItemStack().getItemMeta() == null)
					continue;
				if (item.getItemStack().getItemMeta().getDisplayName() == null)
					continue;
				item.setCustomName(item.getItemStack().getItemMeta().getDisplayName());
				item.setCustomNameVisible(true);
			}
		}
	}
}
