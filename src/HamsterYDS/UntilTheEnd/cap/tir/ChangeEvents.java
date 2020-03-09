package HamsterYDS.UntilTheEnd.cap.tir;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;

public class ChangeEvents implements Listener {
    public static ArrayList<UUID> movingPlayers = new ArrayList<UUID>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        movingPlayers.add(player.getUniqueId());
        new BukkitRunnable() {

            @Override
            public void run() {
                movingPlayers.remove(movingPlayers.lastIndexOf(player.getUniqueId()));
                cancel();
            }

        }.runTaskLaterAsynchronously(Tiredness.plugin, 60L);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        PlayerManager.change(player, CheckType.TIREDNESS, 3);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        PlayerManager.change(player, CheckType.TIREDNESS, 1);
    }
}
