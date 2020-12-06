package ute.item.basics;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ute.api.event.player.CustomItemInteractEvent;
import ute.item.ItemManager;

public class SpiderGland implements Listener {
    public static double heal = ItemManager.itemAttributes.getDouble("SpiderGland.heal");

    public SpiderGland() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(CustomItemInteractEvent event) {
        Player player=event.getWho();
        if (event.getUteItem().id.equalsIgnoreCase("SpiderGland")) {
            event.setCancelled(true);
            if (player.getHealth() + heal >= player.getMaxHealth())
                player.setHealth(player.getMaxHealth());
            else
                player.setHealth(player.getHealth() + heal);
        }
    }
}
