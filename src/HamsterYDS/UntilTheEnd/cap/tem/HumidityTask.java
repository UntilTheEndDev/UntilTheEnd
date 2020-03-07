package HamsterYDS.UntilTheEnd.cap.tem;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class HumidityTask extends BukkitRunnable {
    public HumidityTask(UntilTheEnd plugin) {
        runTaskTimer(plugin, 0L, 150L);
    }

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            if (!Config.enableWorlds.contains(world)) continue;
            for (Player player : world.getPlayers()) {
                int hum = PlayerManager.check(player.getName(), "hum");
                PlayerManager.change(player.getName(), "tem", -hum / 5);
            }
        }
    }
}
