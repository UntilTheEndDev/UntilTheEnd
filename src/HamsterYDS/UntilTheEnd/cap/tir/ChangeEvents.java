package HamsterYDS.UntilTheEnd.cap.tir;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;
import org.bukkit.scheduler.BukkitTask;

public class ChangeEvents implements Listener {
    public UntilTheEnd plugin;

    public ChangeEvents(UntilTheEnd plugin) {
        this.plugin = plugin;
    }

    public static Map<UUID, BukkitTask> movingPlayers = new ConcurrentHashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        movingPlayers.computeIfAbsent(player.getUniqueId(), uid -> new BukkitRunnable() {
            private int tick;

            @Override
            public void run() {
                if (tick++ == 0) {
                    movingPlayers.compute(uid, (u, t) -> {
                        if (t == null) {
                            return null;
                        }
                        if (t.getTaskId() == getTaskId()) {
                            return null;
                        }
                        return t;
                    });
                    cancel();
                } else {
                    final BukkitTask task = movingPlayers.get(uid);
                    if (task == null || task.getTaskId() != getTaskId())
                        cancel();
                }
            }
        }.runTaskTimerAsynchronously(UntilTheEnd.getInstance(), 0, 0));
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        try {
        if (event.getTo().distance(event.getFrom()) <= 10.0) return;
        PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.teleport"));
        }catch(Exception e) {
        	return;
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player == null)
            return;
        PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.break"));
    }

    @EventHandler
    public void onTalk(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player == null)
            return;
        PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.talk"));
    }

    @EventHandler
    public void onTab(PlayerChatTabCompleteEvent event) {
        Player player = event.getPlayer();
        if (player == null)
            return;
        PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.tab"));
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.damage"));
        }
        if (event.getEntity() instanceof Player) {
            if (!(event.getDamager() instanceof LivingEntity)) return;
            Player player = (Player) event.getEntity();
            PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.beDamaged"));
        }
    }

    @EventHandler
    public void onPick(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.enchant"));
    }
}
