package ute.player.death;

import org.bukkit.event.Listener;

import java.util.HashMap;

public class DeathMessage implements Listener {
    public static HashMap<String, DeathCause> causes = new HashMap<String, DeathCause>();

//    @EventHandler(priority = EventPriority.LOWEST)
//    public void onDeath(PlayerDeathEvent event) {
//        Player player = event.getEntity();
//        if (causes.containsKey(player.getName())) {
//            DeathCause cause = causes.get(player.getName());
//            switch (cause) {
//                case COLDNESS:
//                    broadDeathMessage(UTEi18n.cache("death.coldness").replace("{player}", player.getName()));
//                    break;
//                case HOTNESS:
//                    broadDeathMessage(UTEi18n.cache("death.hotness").replace("{player}", player.getName()));
//                    break;
//                case DARKNESS:
//                    broadDeathMessage(UTEi18n.cache("death.darkness").replace("{player}", player.getName()));
//                    break;
//                case BEEMINE:
//                    broadDeathMessage(UTEi18n.cache("death.beemine").replace("{player}", player.getName()));
//                    break;
//                case TOOTHTRAP:
//                    broadDeathMessage(UTEi18n.cache("death.toothtrap").replace("{player}", player.getName()));
//                    break;
//                case BLOWARROW:
//                    broadDeathMessage(UTEi18n.cache("death.blowarrow").replace("{player}", player.getName()));
//                    break;
//                case INVALIDSLEEPNESS:
//                    broadDeathMessage(UTEi18n.cache("death.invalidsleepness").replace("{player}", player.getName()));
//                    break;
//                default:
//                    break;
//            }
//            causes.remove(player.getName());
//        }
//    }
//
//    private void broadDeathMessage(String replace) {
//        for(Player player: Bukkit.getOnlinePlayers())
//            player.sendMessage(replace);
//    }
}
