package ute.player.role.tasks;

import ute.internal.NPCChecker;
import ute.internal.ResidenceChecker;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ute.Config;
import ute.UntilTheEnd;
import ute.api.PlayerApi;
import ute.player.PlayerManager;
import ute.player.PlayerManager.CheckType;
import ute.player.role.Roles;

public class Willow {
    public Willow(UntilTheEnd plugin) {
        new Beard().runTaskTimer(plugin, 0L, 60L);
    }

    public static class Beard extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player) || ResidenceChecker.isProtected(player.getLocation())) continue;
                    if (PlayerApi.RoleOperations.getRole(player) == Roles.WILLOW) {
                        Location loc = player.getLocation();
                        for (int x = -5; x <= 5; x++)
                            for (int y = -5; y <= 5; y++)
                                for (int z = -5; z <= 5; z++) {
                                    Location newLoc = loc.clone().add(x, y, z);
                                    switch (newLoc.getBlock().getType()) { 
                                        case FIRE: 
                                            PlayerManager.change(player, CheckType.SANITY, 1);
                                            break;
                                        case LAVA:
                                            PlayerManager.change(player, CheckType.SANITY, 2);
                                            break;
                                        case STATIONARY_LAVA:
                                            PlayerManager.change(player, CheckType.SANITY, 2);
                                            break;
                                        case WATER:
                                            PlayerManager.change(player, CheckType.SANITY, -1);
                                            break;
                                        case STATIONARY_WATER:
                                            PlayerManager.change(player, CheckType.SANITY, -1);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                        if (PlayerManager.check(player, CheckType.HUMIDITY) >= 3) {
                            PlayerManager.change(player, CheckType.SANITY, -2);
                        }
                    }
                }
        }
    }
}
