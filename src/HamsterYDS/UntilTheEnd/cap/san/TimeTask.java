package HamsterYDS.UntilTheEnd.cap.san;

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
public class TimeTask extends BukkitRunnable {
    int counter = 0;

    public TimeTask(UntilTheEnd plugin) {
        runTaskTimer(plugin, 0L, 150L);
    }

    @Override
    public void run() {
        counter++;
        for (World world : Bukkit.getWorlds()) {
            if (!Config.enableWorlds.contains(world)) continue;
            long time = world.getTime();
            if (counter % 2 == 0)
                if (time >= 13000 && time <= 16000)
                    for (Player player : world.getPlayers())
                        PlayerManager.change(player.getName(), "san", -1);
            if (time >= 16000 && time <= 22000)
                for (Player player : world.getPlayers())
                    PlayerManager.change(player.getName(), "san", -1);
        }
    }
}
