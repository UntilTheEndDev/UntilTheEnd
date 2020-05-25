package HamsterYDS.UntilTheEnd.world.cave;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.BlockApi;
import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CaveEvents implements Listener {
    public static boolean isEarthQuaking = false;
    public static int lastPeriod = 0;

    public CaveEvents() {
        new CaveQuakeTrigger().runTaskTimerAsynchronously(UntilTheEnd.getInstance(), 0L, 20L);
        new RockFallingManager().runTaskTimer(UntilTheEnd.getInstance(), 0L, 30L);
        new EyeDirectionChanger().runTaskTimer(UntilTheEnd.getInstance(), 0L, 5L);
        Bukkit.getPluginManager().registerEvents(this, UntilTheEnd.getInstance());
    }

    static class CaveQuakeTrigger extends BukkitRunnable {

        @Override
        public void run() {
            lastPeriod = lastPeriod - 1;
            if (lastPeriod <= 0) {
                isEarthQuaking = false;
            }
            if (!isEarthQuaking) {
                if (Math.random() <= 0.01) {
                    isEarthQuaking = true;
                    lastPeriod = (int) (Math.random() * 120);
                }
            }
        }

    }

    // 视角摇晃
    static class EyeDirectionChanger extends BukkitRunnable {

        @Override
        public void run() {
            if (!isEarthQuaking)
                return;

            for (Player player : CaveManager.cave.getPlayers()) {
                Location loc = player.getLocation();
                if (Math.random() <= 0.02) {
                    loc.setYaw((float) (loc.getYaw() + Math.random() * 3 - Math.random() * 3));
                    loc.setPitch((float) (loc.getPitch() + Math.random() * 3 - Math.random() * 3));
                    player.teleport(loc);
                }

                if (Math.random() <= 0.2)
                    player.setVelocity(loc.subtract(loc.clone().add(0, 5, 0)).toVector());
                if (Math.random() <= 0.2)
                    player.setVelocity(loc.subtract(loc.clone().add(0, -5, 0)).toVector());
            }
        }
    }

    // 石块掉落
    static class RockFallingManager extends BukkitRunnable {

        @Override
        public void run() {
            if (!isEarthQuaking)
                return;
            for (Player player : CaveManager.cave.getPlayers()) {
                if (NPCChecker.isNPC(player)) continue;
                Location loc = player.getLocation();
                Location fallingLoc = loc.clone();
                for (int i = 0; i <= 100; i++) {
                    if (Math.random() <= 0.2) {
                        fallingLoc.add(Math.random() * 64 - Math.random() * 64, 0,
                                Math.random() * 64 - Math.random() * 64);
                        List<String> hasSupportLocs = new ArrayList<>();
                        boolean tmp = false;
                        for (int y = 3; y <= 62; y++) {
                            fallingLoc.setY(y);
                            if (tmp) {
                                hasSupportLocs.add(BlockApi.locToStr(fallingLoc));
                                continue;
                            }
                            if (fallingLoc.getBlock().getType() == Material.AIR) {
                                y--;
                                tmp = true;
                            }
                        }
                        for (String toString : hasSupportLocs) {
                            Location collapsePos = BlockApi.strToLoc(toString);
                            assert collapsePos != null;
                            Block block = collapsePos.getBlock();
                            FallingBlock entity = CaveManager.cave.spawnFallingBlock(collapsePos, block.getType(),
                                    block.getData());
                            entity.setHurtEntities(true);
                            entity.setDropItem(false);
                            block.setType(Material.AIR);
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    entity.setVelocity(collapsePos.clone().add(Math.random(), 0, Math.random())
                                            .subtract(collapsePos).toVector());
                                    if (entity.isOnGround())
                                        cancel();
                                }

                            }.runTaskTimer(UntilTheEnd.getInstance(), 0L, 20L);

                        }
                        fallingLoc = loc.clone();
                    }
                }
            }
        }

    }
}
