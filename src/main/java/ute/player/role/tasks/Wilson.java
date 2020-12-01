package ute.player.role.tasks;

import ute.internal.NPCChecker;
import ute.internal.ResidenceChecker;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ute.Config;
import ute.UntilTheEnd;
import ute.api.PlayerApi;
import ute.item.ItemManager;
import ute.player.role.Roles;

public class Wilson {
    public Wilson(UntilTheEnd plugin) {
        new Beard().runTaskTimer(plugin, 0L, 1200L);
    }

    public static class Beard extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)|| ResidenceChecker.isProtected(player.getLocation())) continue;
                    if (PlayerApi.RoleOperations.getRole(player) == Roles.WILSON) {
                        if (Math.random() <= 0.1) {
                            player.getInventory().addItem(ItemManager.items.get("Beard").item);
                        }
                    }
                }
        }
    }
}
