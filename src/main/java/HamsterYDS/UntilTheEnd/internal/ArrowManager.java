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
        final Vector vec = loc.getDirection();
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
                for (int i = 0; i <= 10; i++) {
                    loc.add(vec);
                    if (loc.getBlock().getType() != Material.AIR
                            &&loc.getBlock().getType() != Material.SAPLING
                            &&loc.getBlock().getType() != Material.GRASS
                            &&loc.getBlock().getType() != Material.DEAD_BUSH
                            &&loc.getBlock().getType() != Material.YELLOW_FLOWER
                            &&loc.getBlock().getType() != Material.RED_ROSE
                            &&loc.getBlock().getType() != Material.BROWN_MUSHROOM
                            &&loc.getBlock().getType() != Material.RED_MUSHROOM
                            &&loc.getBlock().getType() != Material.TORCH
                            &&loc.getBlock().getType() != Material.LADDER
                            &&loc.getBlock().getType() != Material.SNOW
                            &&loc.getBlock().getType() != Material.VINE
                            &&loc.getBlock().getType() != Material.WATER_LILY
                            &&loc.getBlock().getType() != Material.CARPET
                            &&loc.getBlock().getType() != Material.DOUBLE_PLANT
                            &&loc.getBlock().getType() != Material.PAINTING
                            &&loc.getBlock().getType() != Material.SIGN
                            &&loc.getBlock().getType() != Material.ITEM_FRAME
                            &&loc.getBlock().getType() != Material.FLOWER_POT
                            &&loc.getBlock().getType() != Material.ARMOR_STAND
                            &&loc.getBlock().getType() != Material.BANNER
                            &&loc.getBlock().getType() != Material.END_CRYSTAL
                            &&loc.getBlock().getType() != Material.LEVER
                            &&loc.getBlock().getType() != Material.GOLD_PLATE
                            &&loc.getBlock().getType() != Material.STONE_PLATE
                            &&loc.getBlock().getType() != Material.WOOD_PLATE
                            &&loc.getBlock().getType() != Material.IRON_PLATE
                            &&loc.getBlock().getType() != Material.REDSTONE_TORCH_OFF
                            &&loc.getBlock().getType() != Material.REDSTONE_TORCH_ON
                            &&loc.getBlock().getType() != Material.STONE_BUTTON
                            &&loc.getBlock().getType() != Material.TRAP_DOOR
                            &&loc.getBlock().getType() != Material.TRIPWIRE_HOOK
                            &&loc.getBlock().getType() != Material.WOOD_BUTTON
                            &&loc.getBlock().getType() != Material.IRON_TRAPDOOR
                            && (!loc.getBlock().getType().toString().contains("DOOR"))
                            &&loc.getBlock().getType() != Material.REDSTONE
                            &&loc.getBlock().getType() != Material.REDSTONE_COMPARATOR
                            &&loc.getBlock().getType() != Material.REDSTONE_COMPARATOR_OFF
                            &&loc.getBlock().getType() != Material.REDSTONE_COMPARATOR_ON
                            &&loc.getBlock().getType() != Material.DIODE_BLOCK_OFF
                            &&loc.getBlock().getType() != Material.DIODE_BLOCK_ON
                            &&loc.getBlock().getType() != Material.DIODE
                            && (!loc.getBlock().getType().toString().contains("RAIL"))
                            &&loc.getBlock().getType() != Material.STRING
                            &&loc.getBlock().getType() != Material.BREWING_STAND
                    ) {
                        armor.getWorld().spawnParticle(Particle.CRIT, loc.clone().add(0, 1, 0), 1);
                        loc.add(vec);
                        armor.teleport(loc);
                        if (block != null)
                            block.accept(loc.getBlock());
                        clear();
                        return;
                    }
                    for (Entity entity : armor.getWorld().getNearbyEntities(loc.clone().add(0, 0.5, 0), range, range, range)) {
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
