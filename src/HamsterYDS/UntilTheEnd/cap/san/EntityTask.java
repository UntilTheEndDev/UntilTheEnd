package HamsterYDS.UntilTheEnd.cap.san;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class EntityTask extends BukkitRunnable {
    public EntityTask(UntilTheEnd plugin) {
        runTaskTimer(plugin, 0L, 100L);
    }

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            if (!Config.enableWorlds.contains(world)) continue;
            for (Player player : world.getPlayers()) {
                for (Entity entity : player.getNearbyEntities(5.0, 5.0, 5.0)) {
                    if (entity instanceof Player) PlayerManager.change(player.getName(), "san", 1);
                    if (entity instanceof Monster) PlayerManager.change(player.getName(), "san", -1);
                }
            }
        }
    }
}
