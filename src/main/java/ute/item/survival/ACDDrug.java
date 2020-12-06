package ute.item.survival;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ute.api.event.player.CustomItemInteractEvent;
import ute.item.ItemManager;

public class ACDDrug implements Listener {
    public ACDDrug() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(CustomItemInteractEvent event) {
        Player player=event.getWho();
        if (event.getUteItem().id.equalsIgnoreCase("ACDDrug")) {
            event.setCancelled(true);
            if (player.getMaxHealth() + 6.0 >= 40.0) player.setMaxHealth(40.0);
            else player.setMaxHealth(player.getMaxHealth() + 6.0);
        }
    }
}
