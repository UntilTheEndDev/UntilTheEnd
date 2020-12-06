package ute.cap.tir;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ute.Config;
import ute.Logging;
import ute.UntilTheEnd;
import ute.api.PlayerApi;
import ute.api.event.cap.TirednessChangeEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class ChangeEvents implements Listener {

    // configuration
    private static final double
            CHANGE_EVENT_TELEPORT = Tiredness.yaml.getDouble("change.event.teleport"),
            CHANGE_EVENT_TAB = Tiredness.yaml.getDouble("change.event.tab"),
            CHANGE_EVENT_DAMAGE = Tiredness.yaml.getDouble("change.event.damage"),
            CHANGE_EVENT_BE_DAMAGED = Tiredness.yaml.getDouble("change.event.beDamaged"),
            CHANGE_EVENT_ENCHANT = Tiredness.yaml.getDouble("change.event.enchant"),
            CHANGE_EVENT_BREAK = Tiredness.yaml.getDouble("change.event.break"),
            CHANGE_EVENT_TALK = Tiredness.yaml.getDouble("change.event.talk");

    public UntilTheEnd plugin;

    public ChangeEvents(UntilTheEnd plugin) {
        this.plugin = plugin;
    }

    public static Map<UUID, BukkitTask> movingPlayers = new ConcurrentHashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (!Config.enableWorlds.contains(event.getFrom().getWorld())) return;
        if (!Config.enableWorlds.contains(event.getTo().getWorld())) return;
        try {
            if (event.getTo().getWorld() != event.getFrom().getWorld()) return;
            if (event.getTo().distance(event.getFrom()) <= 10.0) return;
            PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.TELEPORT,CHANGE_EVENT_TELEPORT);
        } catch (Exception e) {
            Logging.getLogger().log(Level.SEVERE, "Failed to process teleport event! ", e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player == null)
            return;
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.BREAK,CHANGE_EVENT_BREAK);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTalk(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player == null)
            return;
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.TALK,CHANGE_EVENT_TALK);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTab(PlayerChatTabCompleteEvent event) {
        Player player = event.getPlayer();
        if (player == null)
            return;
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.TAB,CHANGE_EVENT_TAB);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!Config.enableWorlds.contains(event.getDamager().getWorld())) return;
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.DAMAGE,CHANGE_EVENT_DAMAGE);
        }
        if (event.getEntity() instanceof Player) {
            if (!(event.getDamager() instanceof LivingEntity)) return;
            Player player = (Player) event.getEntity();
            PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.BE_DAMAGED,CHANGE_EVENT_BE_DAMAGED);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEnchant(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.ENCHANT,CHANGE_EVENT_ENCHANT);
    }
}
