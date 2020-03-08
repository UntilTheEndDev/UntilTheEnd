package HamsterYDS.UntilTheEnd.cap;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
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
        BossBar san, tem, hum;
    }

    private static final Deque<NdBossBar> released = new ConcurrentLinkedDeque<>();
    private static final Map<UUID, NdBossBar> bars = new HashMap<>();

    private static NdBossBar create(UUID key) {
        return bars.compute(key, (k, v) -> {
            if (v != null) return v;
            final NdBossBar old = released.poll();
            if (old != null) return old;
            NdBossBar nd = new NdBossBar();
            nd.san = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);
            nd.tem = Bukkit.createBossBar("", BarColor.PURPLE, BarStyle.SOLID);
            nd.hum = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
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

    private static void release(UUID uid) {
        final NdBossBar remove = bars.remove(uid);
        if (remove != null) {
            remove.san.removeAll();
            remove.tem.removeAll();
            remove.hum.removeAll();
            released.add(remove);
        }
    }

    @Override
    public void run() {
        for (World w : Config.enableWorlds) {
            for (Player p : w.getPlayers()) {
                final NdBossBar ndBossBar = create(p.getUniqueId());
                int san = PlayerManager.check(p, PlayerManager.CheckType.SANITY);
                int tem = PlayerManager.check(p, PlayerManager.CheckType.TEMPERATURE);
                int hum = PlayerManager.check(p, PlayerManager.CheckType.HUMIDITY);
                ndBossBar.san.setTitle("§6§l" + HudProvider.sanity.get(p.getName()) + "  §c§l理智   §r§b§l" + san + "  §6§l" + HudProvider.sanity.get(p.getName()));
                ndBossBar.san.setProgress(san / 200.0);
                ndBossBar.san.addPlayer(p);

                ndBossBar.tem.setTitle("§6§l" + HudProvider.temperature.get(p.getName()) + "  §e§l温度   §r§b§l" + tem + "  §6§l" + HudProvider.temperature.get(p.getName()));
                ndBossBar.tem.setProgress((tem + 5) / 85.0);
                ndBossBar.tem.addPlayer(p);
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
                    ndBossBar.hum.setTitle("§6§l" + HudProvider.humidity.get(p.getName()) + "  §3§l湿度   §r§b§l" + hum + "  §6§l" + HudProvider.humidity.get(p.getName()));
                    ndBossBar.hum.setProgress(hum / 100.0);
                    ndBossBar.hum.addPlayer(p);
                }
            }
        }
		/*
		for(World world:Config.enableWorlds)
			for(Player player:world.getPlayers()) { 
				int san=PlayerManager.check(player,"san");
				int tem=PlayerManager.check(player,"tem");
				int hum=PlayerManager.check(player,"hum");
				BossBar bars=(!bossBars.containsKey(player.getName()))?Bukkit.createBossBar("",BarColor.GREEN,BarStyle.SOLID,BarFlag.CREATE_FOG):bossBars.get(player.getName());
				bars.setTitle("§6§l"+HudProvider.sanity.get(player.getName())+"  §c§l理智   §r§b§l"+san+"  §6§l"+HudProvider.sanity.get(player.getName()));
				bars.setProgress(((float)san/200.0));
				bars.addPlayer(player);
				
				BossBar bart=(!bossBart.containsKey(player.getName()))?Bukkit.createBossBar("",BarColor.PURPLE,BarStyle.SOLID,BarFlag.CREATE_FOG):bossBart.get(player.getName());
				bart.setTitle("§6§l"+HudProvider.temperature.get(player.getName())+"  §e§l温度   §r§b§l"+tem+"  §6§l"+HudProvider.temperature.get(player.getName()));
				bart.setProgress((((float)tem+5)/85.0));
				bart.addPlayer(player);
				
				BossBar barh=(!bossBarh.containsKey(player.getName()))?Bukkit.createBossBar("",BarColor.BLUE,BarStyle.SOLID,BarFlag.CREATE_FOG):bossBarh.get(player.getName());
				barh.setTitle("§6§l"+HudProvider.humidity.get(player.getName())+"  §3§l湿度   §r§b§l"+hum+"  §6§l"+HudProvider.humidity.get(player.getName()));
				barh.setProgress(((float)hum/100.0));
				if((hum!=0||world.hasStorm())&&(player.getGameMode()!=GameMode.CREATIVE&&player.getGameMode()!=GameMode.SPECTATOR)) 
					barh.addPlayer(player);
				bossBars.remove(player.getName());
				bossBart.remove(player.getName());
				bossBarh.remove(player.getName());
				bossBars.put(player.getName(),bars);
				bossBart.put(player.getName(),bart);
				bossBarh.put(player.getName(),barh);
			}
		*/
    }
}
