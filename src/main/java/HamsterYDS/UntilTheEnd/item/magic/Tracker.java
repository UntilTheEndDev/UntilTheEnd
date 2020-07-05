package HamsterYDS.UntilTheEnd.item.magic;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

public class Tracker {
    public class TrackingArrow {
        public Arrow arrow;
        public Player victim;

        public TrackingArrow(Arrow arrow, Player victim) {
            this.arrow = arrow;
            this.victim = victim;
            startTracking();
        }

        public void startTracking() {
            new BukkitRunnable() {
                Location arrowLoc = arrow.getLocation();
                Location victimLoc = victim.getLocation();
                Location targetBlock = victimLoc.clone();
                Vector currentDirection = arrowLoc.subtract(targetBlock).toVector();

                @Override
                public void run() {
                    // 玩家下线或切换世界
                    if ((!victim.isOnline())|| 
                        (!victim.getWorld().getName().equalsIgnoreCase(arrowLoc.getWorld().getName()))) {
                        cancel();
                        return;
                    }
                    // 弓箭消失了
                    if (arrow.isDead()) {
                        cancel();
                        return;
                    }
                    // 弓箭仍然在运动
                    if (arrow.getLocation() != arrowLoc) {
                        arrowLoc = arrow.getLocation();
                    } else {
                        cancel();
                        return;
                    }

                    if (arrow.getLocation().distance(targetBlock) <= 0.1) {
                        targetBlock = victimLoc.clone();
                        currentDirection = arrowLoc.subtract(victimLoc).toVector();
                    }else {
                        arrow.setVelocity(currentDirection);
                    }
                }
            }.runTaskTimer(UntilTheEnd.getInstance(), 0L, 1L);
        }
    }
}
