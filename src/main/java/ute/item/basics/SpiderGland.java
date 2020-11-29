package ute.item.basics;

import ute.Config;
import ute.internal.DisableManager;
import ute.internal.EventHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import ute.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class SpiderGland implements Listener {
    public static double heal = ItemManager.itemAttributes.getDouble("SpiderGland.heal");

    public SpiderGland() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRight(PlayerInteractEvent event) {
        if (event.isCancelled() && !DisableManager.bypass_right_action_cancelled) return;
        Player player = event.getPlayer();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        if (!EventHelper.isRight(event.getAction()))
            return;
        if (!event.hasItem()) return;
        ItemStack item = event.getItem();
        if (ItemManager.isSimilar(item, getClass())) {
            event.setCancelled(true);
            if (player.getHealth() + heal >= player.getMaxHealth())
                player.setHealth(player.getMaxHealth());
            else
                player.setHealth(player.getHealth() + heal);
        }
    }
}
