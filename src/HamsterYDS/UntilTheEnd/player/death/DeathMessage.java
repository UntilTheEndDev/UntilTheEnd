package HamsterYDS.UntilTheEnd.player.death;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import HamsterYDS.UntilTheEnd.Config;

public class DeathMessage implements Listener {
    public static HashMap<String, DeathCause> causes = new HashMap<String, DeathCause>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (causes.containsKey(player.getName())) {
            DeathCause cause = causes.get(player.getName());
            switch (cause) {
                case COLDNESS:
                    event.setDeathMessage(Config.yaml.getString("death.coldness", "<language death.coldness>").replace("{player}", player.getName()));
                    break;
                case HOTNESS:
                    event.setDeathMessage(Config.yaml.getString("death.hotness", "<language death.hotness>").replace("{player}", player.getName()));
                    break;
                case DARKNESS:
                    event.setDeathMessage(Config.yaml.getString("death.darkness", "<language death.darkness>").replace("{player}", player.getName()));
                    break;
                case BEEMINE:
                    event.setDeathMessage(Config.yaml.getString("death.beemine", "<language death.beemine>").replace("{player}", player.getName()));
                    break;
                case TOOTHTRAP:
                    event.setDeathMessage(Config.yaml.getString("death.toothtrap", "<language death.toothtrap>").replace("{player}", player.getName()));
                    break;
                case BLOWARROW:
                    event.setDeathMessage(Config.yaml.getString("death.blowarrow", "<language death.blowarrow>").replace("{player}", player.getName()));
                    break;
                case INVALIDSLEEPNESS:
                    event.setDeathMessage(Config.yaml.getString("death.invalidsleepness", "<language death.invalidsleepness>").replace("{player}", player.getName()));
                    break;
                default:
                    break;
            }
            causes.remove(player.getName());
        }
    }
}
