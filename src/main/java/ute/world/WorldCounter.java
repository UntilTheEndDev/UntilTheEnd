package ute.world;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import ute.internal.NPCChecker;
import ute.internal.UTEi18n;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ute.Config;
import ute.cap.tem.TemperatureProvider;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class WorldCounter extends BukkitRunnable {
    // private HashSet<String> changingWorlds=new HashSet<String>();
    private final Map<UUID, AtomicLong> lateTime = new HashMap<>();

    @Override
    public void run() {
        for (World world : Config.enableWorlds) {
            long time = world.getTime();
            UUID uid = world.getUID();
            final AtomicLong atomicLong = lateTime.get(uid);
            if (atomicLong == null) {
                lateTime.put(uid, new AtomicLong(time));
                // 没值的时候到底需不需要把这玩意进入下一天
                // newDay(world);
            } else {
                if (atomicLong.get() > time) { // Loop
                    newDay(world);
                }
                atomicLong.set(time);
            }
        }
    }

    private void newDay(World world) {
        WorldProvider.IWorld state = WorldProvider.worldStates.get(world.getName());
        // int days = state.day;
        if (state.season != WorldProvider.Season.NULL) {
            if (state.day < state.loop) {
                state.day++;
            } else {
                state.day = 1;
                state.loop = (state.season = state.season.next()).newLoop();
            }
        }
        WorldProvider.saveWorlds();
        tellPlayers(world);
        TemperatureProvider.loadWorldTemperature(world);
    }

    public static void tellPlayers(World world) {
        WorldProvider.IWorld state = WorldProvider.worldStates.get(world.getName());
        for (Player player : world.getPlayers())
            if (!NPCChecker.isNPC(player))
                player.sendTitle(UTEi18n.parse("mechanism.world.next-day.main", state.season.name), UTEi18n.parse("mechanism.world.next-day.sub", String.valueOf(state.day)), 10, 70, 20);
    }
}
