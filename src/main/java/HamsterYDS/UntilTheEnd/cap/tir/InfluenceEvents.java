package HamsterYDS.UntilTheEnd.cap.tir;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class InfluenceEvents implements Listener {
    public static UntilTheEnd plugin = UntilTheEnd.getInstance();
    private static final double teleport = Tiredness.yaml.getDouble("influence.event.teleport");
    private static final double lbreak = Tiredness.yaml.getDouble("influence.event.break");
    private static final double place = Tiredness.yaml.getDouble("influence.event.place");
    private static final double talk = Tiredness.yaml.getDouble("influence.event.talk");
    private static final double attack = Tiredness.yaml.getDouble("influence.event.attack");

    public InfluenceEvents() {
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (!Config.enableWorlds.contains(event.getFrom().getWorld())) return;
        if (!Config.enableWorlds.contains(event.getTo().getWorld())) return;
        if (PlayerManager.check(player, CheckType.TIREDNESS) >= teleport) {
            player.sendMessage(UTEi18n.cache("cap.tir.influence.teleport"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        if (PlayerManager.check(player, CheckType.TIREDNESS) >= lbreak) {
            player.sendMessage(UTEi18n.cache("cap.tir.influence.break"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        if (!Config.enableWorlds.contains(player.getWorld())) return;
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
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        if (PlayerManager.check(player, CheckType.TIREDNESS) >= talk) {
            player.sendMessage(UTEi18n.cache("cap.tir.influence.talk"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!Config.enableWorlds.contains(event.getDamager().getWorld())) return;
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (PlayerManager.check(player, CheckType.TIREDNESS) >= attack) {
                player.sendMessage(UTEi18n.cache("cap.tir.influence.attack"));
                event.setCancelled(true);
            }
        }
    }
}
