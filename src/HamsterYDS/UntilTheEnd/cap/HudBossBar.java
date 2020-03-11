package HamsterYDS.UntilTheEnd.cap;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class HudBossBar extends BukkitRunnable implements Listener {
    private static class NdBossBar {
        BossBar san, tem, hum, tir;
    }

    private static final Deque<NdBossBar> released = new ConcurrentLinkedDeque<>();
    private static final Map<UUID, NdBossBar> bars = new HashMap<>();

    public static NdBossBar create(UUID key) {
        return bars.compute(key, (k, v) -> {
            if (v != null) return v;
            final NdBossBar old = released.poll();
            if (old != null) return old;
            NdBossBar nd = new NdBossBar();
            nd.san = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);
            nd.tem = Bukkit.createBossBar("", BarColor.PURPLE, BarStyle.SOLID);
            nd.hum = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
            nd.tir = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID);
            return nd;
        });
    }

    @EventHandler
    void onPlayerWorldTeleport(PlayerChangedWorldEvent event) {
        if (!Config.enableWorlds.contains(event.getPlayer().getWorld())) {
            release(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    void onPlayerLeft(PlayerQuitEvent event) {
        release(event.getPlayer().getUniqueId());
    }

    HudBossBar() {
        Bukkit.getPluginManager().registerEvents(this, UntilTheEnd.getInstance());
    }

    public static void release(UUID uid) {
        final NdBossBar remove = bars.remove(uid);
        if (remove != null) {
            remove.san.removeAll();
            remove.tem.removeAll();
            remove.hum.removeAll();
            remove.tir.removeAll();
            released.add(remove);
        }
    }

    @Override
    public void run() {
        for (World w : Config.enableWorlds) {
            for (Player p : w.getPlayers()) {
                final NdBossBar ndBossBar = create(p.getUniqueId());
                double san = PlayerManager.check(p, PlayerManager.CheckType.SANITY);
                double sanMax = PlayerManager.check(p, PlayerManager.CheckType.SANMAX);
                double tem = PlayerManager.check(p, PlayerManager.CheckType.TEMPERATURE);
                double hum = PlayerManager.check(p, PlayerManager.CheckType.HUMIDITY);
                double tir = PlayerManager.check(p, PlayerManager.CheckType.TIREDNESS);
                ndBossBar.san.setTitle(UTEi18n.parse("hud.bossbar.sanity", HudProvider.sanity.get(p.getName()), String.valueOf(san), HudProvider.sanity.get(p.getName())));
                ndBossBar.san.setProgress(san / sanMax);
                ndBossBar.san.addPlayer(p);

                ndBossBar.tem.setTitle(UTEi18n.parse("hud.bossbar.temperature", HudProvider.temperature.get(p.getName()), String.valueOf(tem), HudProvider.temperature.get(p.getName())));
                ndBossBar.tem.setProgress((tem + 5) / 85.0);
                ndBossBar.tem.addPlayer(p);

                ndBossBar.tir.setTitle(UTEi18n.parse("hud.bossbar.tiredness", HudProvider.tiredness.get(p.getName()), String.valueOf(tir), HudProvider.tiredness.get(p.getName())));
                ndBossBar.tir.setProgress(tir / 100.0);
                ndBossBar.tir.addPlayer(p);
                boolean hiddenHum;
                switch (p.getGameMode()) {
                    case CREATIVE:
                    case SPECTATOR:
                        hiddenHum = true;
                        break;
                    default:
                        if (w.hasStorm()) {
                            hiddenHum = false;
                        } else {
                            hiddenHum = hum == 0;
                        }
                        break;
                }
                if (hiddenHum) {
                    ndBossBar.hum.removeAll();
                } else {
                    ndBossBar.hum.setTitle(UTEi18n.parse("hud.bossbar.humidity", HudProvider.tiredness.get(p.getName()), String.valueOf(hum), HudProvider.humidity.get(p.getName())));
                    ndBossBar.hum.setProgress(hum / 100.0);
                    ndBossBar.hum.addPlayer(p);
                }
            }
        }
    }
}
