package ute.player.role.events;

import ute.Config;
import ute.Logging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import ute.player.PlayerManager;
import ute.player.PlayerManager.CheckType;

public class GeneralEvents implements Listener {
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!Config.enableWorlds.contains(event.getDamager().getWorld())) return;
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            double damageLevel = PlayerManager.check(player, CheckType.DAMAGELEVEL);
            double dm = event.getDamage();
            event.setDamage(dm * damageLevel);
            Logging.getLogger().fine(() -> "Role'd damage{source=" + dm + ", lv=" + damageLevel + ", changed=" + event.getDamage() + "}");
        }
    }
}
