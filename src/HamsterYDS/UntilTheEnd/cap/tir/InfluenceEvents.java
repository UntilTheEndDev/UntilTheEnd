package HamsterYDS.UntilTheEnd.cap.tir;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;

public class InfluenceEvents implements Listener {
    public static UntilTheEnd plugin;
    public static int teleport = Tiredness.yaml.getInt("influence.event.teleport");
    public static int lbreak = Tiredness.yaml.getInt("influence.event.break");
    public static int place = Tiredness.yaml.getInt("influence.event.place");
    public static int talk = Tiredness.yaml.getInt("influence.event.talk");
    public static int attack = Tiredness.yaml.getInt("influence.event.attack");

    public InfluenceEvents(UntilTheEnd plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (PlayerManager.check(player, CheckType.TIREDNESS) >= teleport) {
            player.sendMessage(UTEi18n.cache("cap.tir.influence.teleport"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        if (PlayerManager.check(player, CheckType.TIREDNESS) >= lbreak) {
            player.sendMessage(UTEi18n.cache("cap.tir.influence.break"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        if (PlayerManager.check(player, CheckType.TIREDNESS) >= place) {
            player.sendMessage(UTEi18n.cache("cap.tir.influence.place"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTalk(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player == null)
            return;
        if (PlayerManager.check(player, CheckType.TIREDNESS) >= talk) {
            player.sendMessage(UTEi18n.cache("cap.tir.influence.talk"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (PlayerManager.check(player, CheckType.TIREDNESS) >= attack) {
                player.sendMessage(UTEi18n.cache("cap.tir.influence.attack"));
                event.setCancelled(true);
            }
        }
    }
}
