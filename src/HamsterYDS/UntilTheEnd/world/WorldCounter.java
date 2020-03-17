package HamsterYDS.UntilTheEnd.world;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.cap.tem.TemperatureProvider;
import HamsterYDS.UntilTheEnd.world.WorldProvider.IWorld;
import HamsterYDS.UntilTheEnd.world.WorldProvider.Season;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class WorldCounter extends BukkitRunnable {
    // private HashSet<String> changingWorlds=new HashSet<String>();
    private Map<UUID, AtomicLong> lateTime = new HashMap<>();

    @Override
    public void run() {
        for (World world : Config.enableWorlds) {
            long time = world.getTime();
            UUID uid = world.getUID();
            final AtomicLong atomicLong = lateTime.get(uid);
            if (atomicLong == null) {
                lateTime.put(uid, new AtomicLong(time));
                newDay(world);
            } else {
                if (atomicLong.get() > time) { // Loop
                    newDay(world);
                }
                atomicLong.set(time);
            }
        }
    }

    private void newDay(World world) {
        IWorld state = WorldProvider.worldStates.get(world.getName());
        int days = state.day;
        if (state.season != Season.NULL) {
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
        IWorld state = WorldProvider.worldStates.get(world.getName());
        for (Player player : world.getPlayers())
            if (!NPCChecker.isNPC(player))
                player.sendTitle(UTEi18n.parse("mechanism.world.next-day.main", state.season.name), UTEi18n.parse("mechanism.world.next-day.sub", String.valueOf(state.day)), 10, 70, 20);//TODO-lang
    }
}
