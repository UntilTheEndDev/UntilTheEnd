package HamsterYDS.UntilTheEnd.player.role.tasks;

import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import HamsterYDS.UntilTheEnd.internal.ResidenceChecker;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.PlayerApi;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;
import HamsterYDS.UntilTheEnd.player.role.Roles;

public class Wolfgang {
    public Wolfgang(UntilTheEnd plugin) {
        new Beard().runTaskTimer(plugin, 0L, 60L);
    }

    public static class Beard extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)||ResidenceChecker.isProtected(player.getLocation())) continue;
                    if (PlayerApi.getRole(player) == Roles.WOLFGANG) {
                        /*
                        if (world.getTime() >= InfluenceTasks.up &&
                                world.getTime() >= InfluenceTasks.down) {
                            PlayerManager.change(player, CheckType.SANITY, -2);
                        }
                        */
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
