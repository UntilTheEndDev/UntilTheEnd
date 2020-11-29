package ute.player;

import org.bukkit.Bukkit;

import ute.UntilTheEnd;
import ute.player.death.DeathMessage;
import ute.player.role.events.EventLoader;
import ute.player.role.tasks.TaskLoader;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Player {

    public static void initialize(UntilTheEnd plugin) {
        new PlayerManager(plugin);
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers())
            PlayerManager.load(player);
        new PlayerInventoryAdapt(plugin);
        plugin.getServer().getPluginManager().registerEvents(new DeathMessage(), plugin);
        new EventLoader(plugin);
        new TaskLoader(plugin);
    }
}
