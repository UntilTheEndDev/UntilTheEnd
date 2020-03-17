package HamsterYDS.UntilTheEnd.player.role.tasks;

import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.role.Roles;

public class Wilson {
    public ItemStack beard;

    public Wilson(UntilTheEnd plugin) {
        beard = ItemManager.yaml1.getItemStack("胡须");
        new Beard().runTaskTimer(plugin, 0L, 1200L);
    }

    public class Beard extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)) continue;
                    if (UntilTheEndApi.PlayerApi.getRole(player) == Roles.WILSON) {
                        if (Math.random() <= 0.1) {
                            player.getInventory().addItem(beard);
                        }
                    }
                }
        }
    }
}
