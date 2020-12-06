package ute.item.survival;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ute.api.event.player.CustomItemInteractEvent;
import ute.item.ItemManager;

public class HealingSalve implements Listener {
    public HealingSalve() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(CustomItemInteractEvent event) {
        Player player=event.getWho();
        if (event.getUteItem().id.equalsIgnoreCase("HealingSalve")) {
            event.setCancelled(true);
            if (player.getHealth() + 10.0 >= player.getMaxHealth()) player.setHealth(player.getMaxHealth());
            else player.setHealth(player.getHealth() + 10.0);
        }
    }
}
