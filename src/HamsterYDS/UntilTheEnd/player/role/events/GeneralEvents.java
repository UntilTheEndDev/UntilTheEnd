package HamsterYDS.UntilTheEnd.player.role.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;

public class GeneralEvents implements Listener {
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            double damageLevel = PlayerManager.check(player, CheckType.DAMAGELEVEL);
            event.setDamage(event.getDamage() * damageLevel);
        }
    }
}
