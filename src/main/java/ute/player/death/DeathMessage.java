package ute.player.death;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import ute.internal.UTEi18n;

public class DeathMessage implements Listener {
    public static HashMap<String, DeathCause> causes = new HashMap<String, DeathCause>();

    @EventHandler()
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (causes.containsKey(player.getName())) {
            DeathCause cause = causes.get(player.getName());
            switch (cause) {
                case COLDNESS:
                    event.setDeathMessage(UTEi18n.cache("death.coldness").replace("{player}", player.getName()));
                    break;
                case HOTNESS:
                    event.setDeathMessage(UTEi18n.cache("death.hotness").replace("{player}", player.getName()));
                    break;
                case DARKNESS:
                    event.setDeathMessage(UTEi18n.cache("death.darkness").replace("{player}", player.getName()));
                    break;
                case BEEMINE:
                    event.setDeathMessage(UTEi18n.cache("death.beemine").replace("{player}", player.getName()));
                    break;
                case TOOTHTRAP:
                    event.setDeathMessage(UTEi18n.cache("death.toothtrap").replace("{player}", player.getName()));
                    break;
                case BLOWARROW:
                    event.setDeathMessage(UTEi18n.cache("death.blowarrow").replace("{player}", player.getName()));
                    break;
                case INVALIDSLEEPNESS:
                    event.setDeathMessage(UTEi18n.cache("death.invalidsleepness").replace("{player}", player.getName()));
                    break;
                default:
                    break;
            }
            causes.remove(player.getName());
        }
    }
}
