/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/08 23:16:02
 *
 * UntilTheEnd/UntilTheEnd/ArrowManager.java
 */

package HamsterYDS.UntilTheEnd.internal;

import HamsterYDS.UntilTheEnd.item.ItemManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public class ArrowManager {
    public static void startFire(
            Consumer<LivingEntity> hit,
            Consumer<Block> block,
            ItemStack stack,
            Location loc,
            int maxLoop, int autoclear,
            Entity attacker,
            double range) {
        Entity entity = loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        ArmorStand armor = (ArmorStand) entity;
        armor.setSmall(true);
        armor.setItemInHand(stack);
        armor.setVisible(false);
        armor.setBasePlate(false);
        armor.setArms(false);
        armor.setGravity(false);
        armor.setRemoveWhenFarAway(true);
        final Vector vec = loc.getDirection().normalize().multiply(0.7);
        new BukkitRunnable() {
            int dist = 0;

            @Override
            public void run() {
                Location loc = armor.getLocation();
                if (armor.isDead()) {
                    cancel();
                    armor.remove();
                    return;
                }
                for (int i = 0; i <= 15; i++) {
                    loc.add(vec);
                    if (loc.getBlock().getType() != Material.AIR) {
                        armor.getWorld().spawnParticle(Particle.CRIT, loc.clone().add(0, 1, 0), 1);
                        armor.teleport(loc);
                        if (block != null)
                            block.accept(loc.getBlock());
                        clear();
                        return;
                    }
                    for (Entity entity : armor.getWorld().getNearbyEntities(loc.add(0, 0.3, 0), range, range, range)) {
                        if (entity == attacker)
                            continue;
                        if (entity instanceof ArmorStand) continue;
                        if (!(entity instanceof LivingEntity))
                            continue;
                        if (hit != null)
                            hit.accept((LivingEntity) entity);
                        armor.teleport(loc);
                        clear();
                        return;
                    }

                    if (dist++ >= maxLoop) {
                        armor.teleport(loc);
                        if (block != null)
                            block.accept(loc.getBlock());
                        clear();
                        return;
                    }
                }
                armor.teleport(loc);
            }

            public void clear() {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        armor.remove();
                        cancel();
                    }
                }.runTaskLater(ItemManager.plugin,
                        autoclear * 20L);
                cancel();
            }
        }.runTaskTimer(ItemManager.plugin, 2L, 1L);
    }
}
