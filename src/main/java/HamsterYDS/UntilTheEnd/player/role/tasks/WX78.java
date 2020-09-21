package HamsterYDS.UntilTheEnd.player.role.tasks;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.PlayerApi;
import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import HamsterYDS.UntilTheEnd.internal.ResidenceChecker;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;
import HamsterYDS.UntilTheEnd.player.role.Roles;

public class WX78 {
	public WX78(UntilTheEnd plugin) {
        new SetHud().runTaskTimer(plugin, 0L, 40L);
    }

    public static class SetHud extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)||ResidenceChecker.isProtected(player.getLocation())) continue;
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
