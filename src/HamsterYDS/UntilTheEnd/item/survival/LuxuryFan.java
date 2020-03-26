package HamsterYDS.UntilTheEnd.item.survival;

import HamsterYDS.UntilTheEnd.internal.EventHelper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

public class LuxuryFan implements Listener {
    public LuxuryFan() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.hasItem()) return;
        if (!EventHelper.isRight(event.getAction())) return;
        ItemStack item = event.getItem();
        if (ItemManager.isSimilar(item, getClass())) {
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
