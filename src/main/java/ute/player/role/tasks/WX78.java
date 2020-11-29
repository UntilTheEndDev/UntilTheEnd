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

public class WX78 {
	public WX78(UntilTheEnd plugin) {
        new SetHud().runTaskTimer(plugin, 0L, 40L);
    }

    public static class SetHud extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)|| ResidenceChecker.isProtected(player.getLocation())) continue;
                    if (PlayerApi.getRole(player) == Roles.WX78) {
                        player.setMaxHealth(PlayerManager.check(player,CheckType.LEVEL)*4+20);
                        double humidity=PlayerManager.check(player,CheckType.HUMIDITY);
                        //10.0-扣血比率
                        if(humidity>0) {
                        	player.damage(humidity/10.0);
                        }
                    }
                }
        }
    }
}
