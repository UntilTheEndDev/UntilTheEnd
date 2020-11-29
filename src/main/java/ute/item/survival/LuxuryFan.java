package ute.item.survival;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ute.event.player.CustomItemInteractEvent;
import ute.item.ItemManager;
import ute.player.PlayerManager;

public class LuxuryFan implements Listener {
    public LuxuryFan() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(CustomItemInteractEvent event) {
        Player player=event.getWho();
        if (event.getUteItem().id.equalsIgnoreCase("LuxuryFan")) {
            event.setCancelled(true);
            if (PlayerManager.check(player, PlayerManager.CheckType.TEMPERATURE) >= 45)
                PlayerManager.change(player, PlayerManager.CheckType.TEMPERATURE, -30);
            else
                PlayerManager.change(player, PlayerManager.CheckType.TEMPERATURE, 15 - PlayerManager.check(player, PlayerManager.CheckType.TEMPERATURE));
            for (Entity entity : player.getNearbyEntities(5.0, 5.0, 5.0)) {
                if (entity instanceof Player) {
                    if (PlayerManager.check((Player) entity, PlayerManager.CheckType.TEMPERATURE) >= 45)
                        PlayerManager.change((Player) entity, PlayerManager.CheckType.TEMPERATURE, -30);
                    else
                        PlayerManager.change((Player) entity, PlayerManager.CheckType.TEMPERATURE, 15 - PlayerManager.check((Player) entity, PlayerManager.CheckType.TEMPERATURE));
                }
            }
        }
    }
}
