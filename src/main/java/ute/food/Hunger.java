package ute.food;

import ute.internal.NPCChecker;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import ute.Config;
import ute.UntilTheEnd;

public class Hunger implements Listener {

    public Hunger(UntilTheEnd plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        new AutoHunger().runTaskTimer(plugin, 0L, 20 * Food.yaml.getLong("food.hunger.speed"));
    }

    @EventHandler()
    public void onJoin(PlayerJoinEvent event) {
        if (!Config.enableWorlds.contains(event.getPlayer().getWorld())) return;
        event.getPlayer().setFoodLevel(10);
    }

    public static class AutoHunger extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers())
                    if (!NPCChecker.isNPC(player))
                        if (!(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR))
                            player.setFoodLevel(player.getFoodLevel() - 1);
        }
    }
}
