package HamsterYDS.UntilTheEnd.player.role.events;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
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
            double dm = event.getDamage();
            event.setDamage(dm * damageLevel);
            UntilTheEnd.getInstance().getLogger().fine(() -> "Role'd damage{source=" + dm + ", lv=" + damageLevel + ", changed=" + event.getDamage() + "}");
        }
    }
}
