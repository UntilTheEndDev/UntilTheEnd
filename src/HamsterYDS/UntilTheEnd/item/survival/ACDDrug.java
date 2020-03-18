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
public class ACDDrug implements Listener {
    public ACDDrug() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!EventHelper.isRight(event.getAction())) return;
        if (!event.hasItem()) return;
        ItemStack item = event.getItem();
        if (ItemManager.isSimilar(item, getClass())) {
            event.setCancelled(true);
            if (!player.isSneaking()) return;
            if (player.getMaxHealth() + 6.0 >= 40.0) player.setMaxHealth(40.0);
            else player.setMaxHealth(player.getMaxHealth() + 6.0);
        }
    }
}
