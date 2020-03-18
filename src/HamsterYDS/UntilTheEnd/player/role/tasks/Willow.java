package HamsterYDS.UntilTheEnd.player.role.tasks;

import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;
import HamsterYDS.UntilTheEnd.player.role.Roles;

public class Willow {
    public Willow(UntilTheEnd plugin) {
        new Beard().runTaskTimer(plugin, 0L, 60L);
    }

    public class Beard extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)) continue;
                    if (UntilTheEndApi.PlayerApi.getRole(player) == Roles.WILLOW) {
                        Location loc = player.getLocation();
                        for (int x = -5; x <= 5; x++)
                            for (int y = -5; y <= 5; y++)
                                for (int z = -5; z <= 5; z++) {
                                    Location newLoc = loc.clone().add(x, y, z);
                                    switch (newLoc.getBlock().getType()) {
                                        case FIRE:
                                            UntilTheEnd.getInstance().getLogger().finer("awa");
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
