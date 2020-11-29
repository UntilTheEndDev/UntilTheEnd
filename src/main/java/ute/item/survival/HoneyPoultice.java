package ute.item.survival;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ute.event.player.CustomItemInteractEvent;
import ute.item.ItemManager;

public class HoneyPoultice implements Listener {
    public HoneyPoultice() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(CustomItemInteractEvent event) {
        Player player=event.getWho();
        if (event.getUteItem().id.equalsIgnoreCase("HoneyPoultice")) {
            event.setCancelled(true);
            if (player.getHealth() + 12.0 >= player.getMaxHealth()) player.setHealth(player.getMaxHealth());
            else player.setHealth(player.getHealth() + 12.0);
        }
    }
}
