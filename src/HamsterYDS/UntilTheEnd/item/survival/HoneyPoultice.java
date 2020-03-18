package HamsterYDS.UntilTheEnd.item.survival;

import HamsterYDS.UntilTheEnd.internal.EventHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class HoneyPoultice implements Listener {
    public HoneyPoultice() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.hasItem()) return;
        if (!EventHelper.isRight(event.getAction())) return;
        ItemStack item = event.getItem();
        if (ItemManager.isSimilar(item, getClass())) {
            event.setCancelled(true);
            if (!player.isSneaking()) return;
            if (player.getHealth() + 12.0 >= player.getMaxHealth()) player.setHealth(player.getMaxHealth());
            else player.setHealth(player.getHealth() + 12.0);
        }
    }
}
