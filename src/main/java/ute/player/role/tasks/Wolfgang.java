package ute.player.role.tasks;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ute.Config;
import ute.UntilTheEnd;
import ute.api.PlayerApi;
import ute.internal.NPCChecker;
import ute.internal.ResidenceChecker;
import ute.player.PlayerManager;
import ute.player.PlayerManager.CheckType;
import ute.player.role.Roles;

public class Wolfgang {
    public Wolfgang(UntilTheEnd plugin) {
        new Task().runTaskTimer(plugin, 0L, 60L);
    }

    public static class Task extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)|| ResidenceChecker.isProtected(player.getLocation())) continue;
                    if (PlayerApi.RoleOperations.getRole(player) == Roles.WOLFGANG) {
                        if (world.getTime() >= 14000 &&
                                world.getTime() <=21000) {
                            PlayerManager.change(player, CheckType.SANITY, -2);
                        }
                        double hungerPercent = (double) (player.getFoodLevel()) / 20.0;
                        player.setMaxHealth(hungerPercent * 30);
                        PlayerManager.change(player, CheckType.HEALTHMAX,
                                hungerPercent * 30 - PlayerManager.check(player, CheckType.HEALTHMAX));
                        PlayerManager.change(player, CheckType.DAMAGELEVEL,
                                hungerPercent * 1.5 - PlayerManager.check(player, CheckType.DAMAGELEVEL));
                    }
                }
        }
    }
}
