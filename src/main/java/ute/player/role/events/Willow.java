package ute.player.role.events;

import ute.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ute.api.PlayerApi;
import ute.player.role.Roles;

public class Willow implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAttack(EntityDamageEvent event) {
        if (!Config.enableWorlds.contains(event.getEntity().getWorld())) return;
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (PlayerApi.getRole(player) == Roles.WILLOW) {
                if (event.getCause() == DamageCause.FIRE) event.setCancelled(true);
                if (event.getCause() == DamageCause.FIRE_TICK) event.setCancelled(true);
                if (event.getCause() == DamageCause.LAVA) event.setCancelled(true);
            }
        }
    }
}
