package HamsterYDS.UntilTheEnd.cap.tem;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Create at 2020/3/8 1:32
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.cap.tem
 */
public class BiomeTasks extends BukkitRunnable {
    public BiomeTasks() {
        runTaskTimer(UntilTheEnd.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {
        for (World world : Config.enableWorlds)
            for (Player player : world.getPlayers()) {
                final double temperature = player.getLocation().getBlock().getTemperature();
                if (temperature < 0) {
                    PlayerManager.change(player, "tem", -2);
                } else if (temperature < 0.2D) {
                    PlayerManager.change(player, "tem", -1);
                } else if (temperature > 1.2) {
                    PlayerManager.change(player, "tem", (int) temperature);
                }
            }
    }
}
