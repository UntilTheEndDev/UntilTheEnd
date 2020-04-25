package HamsterYDS.UntilTheEnd.player;

import org.bukkit.Bukkit;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.death.DeathMessage;
import HamsterYDS.UntilTheEnd.player.role.events.EventLoader;
import HamsterYDS.UntilTheEnd.player.role.tasks.TaskLoader;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Player {
    public static UntilTheEnd plugin;

    public Player(UntilTheEnd plugin) {
        new PlayerManager(plugin);
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers())
            PlayerManager.load(player);
        new PlayerInventoryAdapt(plugin);
        plugin.getServer().getPluginManager().registerEvents(new DeathMessage(), plugin);
        new EventLoader(plugin);
        new TaskLoader(plugin);
    }
}
