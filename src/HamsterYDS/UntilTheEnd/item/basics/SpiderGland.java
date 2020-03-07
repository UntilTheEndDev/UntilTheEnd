package HamsterYDS.UntilTheEnd.item.basics;

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
public class SpiderGland implements Listener {
	public static double heal = ItemManager.yaml2.getDouble("蜘蛛腺体.heal");
	public SpiderGland() {
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
		ItemManager.cosumeItems.add("SpiderGland");
	}
	@EventHandler
	public void onRight(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!player.isSneaking())
			return;
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		ItemStack item = player.getInventory().getItemInMainHand();
		if (ItemManager.isSimilar(item, ItemManager.namesAndItems.get("§6蜘蛛腺体"))) {
			if (player.getHealth() + heal >= player.getMaxHealth())
				player.setHealth(player.getMaxHealth());
			else
				player.setHealth(player.getHealth() + heal);
		}
	}
}
