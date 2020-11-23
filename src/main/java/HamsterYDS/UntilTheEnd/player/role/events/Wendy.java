package HamsterYDS.UntilTheEnd.player.role.events;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.PlayerApi;
import HamsterYDS.UntilTheEnd.player.role.Roles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Wendy implements Listener {
    public static HashMap<UUID,Long> ghostLastSummonedStamp = new HashMap<UUID,Long>();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamaged(EntityDamageEvent event){
        if (!Config.enableWorlds.contains(event.getEntity().getWorld())) return;
        if(!(event.getEntity() instanceof Player)) return;
        if(((Player) event.getEntity()).getHealth()<=event.getDamage()) return;
        Player player= (Player) event.getEntity();
        if (PlayerApi.getRole(player) == Roles.WENDY) {
            if(ghostLastSummonedStamp.containsKey(player.getUniqueId())){
                long stamp=ghostLastSummonedStamp.get(player.getUniqueId());
                //TODO 常数：鬼魂冷却300冷却 加入Config
                if((System.currentTimeMillis()-stamp)/1000<300){
                    return;
                }
            }
            Location spawnLoc=player.getLocation().clone();
            ArmorStand ghost= (ArmorStand) player.getWorld().spawnEntity(spawnLoc, EntityType.ARMOR_STAND);
            ghost.setCustomNameVisible(true);
            ghost.setCustomName("§6§lAbigail");
            ghost.setVisible(true);
            ghost.setGravity(false);
            player.sendTitle("§6§l鬼魂已召唤","");
            ghostLastSummonedStamp.put(player.getUniqueId(),System.currentTimeMillis());
            new BukkitRunnable(){
                Entity target=ghost;
                @Override
                public void run() {
                    if(ghost.isDead()) {
                        cancel();
                    }
                    ghost.setVelocity(new Vector(Math.random()-Math.random(),Math.random()-Math.random(),Math.random()-Math.random()));
                }
            }.runTaskTimer(UntilTheEnd.getInstance(),0L,3L);
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(ghost.isDead()){
                        cancel();
                    }
                    for(Entity entity:ghost.getNearbyEntities(3,4,3)){
                        if(entity instanceof LivingEntity && entity.getUniqueId()!=player.getUniqueId()
                                                          && entity.getUniqueId()!=ghost.getUniqueId()) {
                            //TODO 数据配置：鬼魂伤害5.0
                            ((LivingEntity) entity).damage(2.5);
                        }
                    }
                }
            }.runTaskTimer(UntilTheEnd.getInstance(),0L,10L);
            new BukkitRunnable(){
                @Override
                public void run() {
                    ghost.damage(1000.0);
                }
                //TODO 存在时间 config
            }.runTaskLater(UntilTheEnd.getInstance(),1200L);
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(ghost.isDead()){
                        cancel();
                    }
                    Location loc=ghost.getLocation();
                    //TODO 粒子效果
                    ghost.getWorld().spawnParticle(Particle.FLAME,ghost.getLocation().add(0,0.5,0),3);
                    ghost.getWorld().spawnParticle(Particle.FLAME,ghost.getLocation().add(0,1,0),3);
                    ghost.getWorld().spawnParticle(Particle.FLAME,ghost.getLocation().add(0.5,0.5,0),3);
                    ghost.getWorld().spawnParticle(Particle.FLAME,ghost.getLocation().add(-0.5,1,0),3);
                    ghost.getWorld().spawnParticle(Particle.FLAME,ghost.getLocation().add(0,0.5,0.5),3);
                    ghost.getWorld().spawnParticle(Particle.FLAME,ghost.getLocation().add(0,1,-0.5),3);
                    ghost.getWorld().spawnParticle(Particle.FLAME,ghost.getLocation().add(0.5,0.5,0.5),3);
                    ghost.getWorld().spawnParticle(Particle.FLAME,ghost.getLocation().add(-0.5,1,-0.5),3);
                    ghost.getWorld().spawnParticle(Particle.FLAME,ghost.getLocation().add(0.5,0.5,-0.5),3);
                    ghost.getWorld().spawnParticle(Particle.FLAME,ghost.getLocation().add(-0.5,1,0.5),3);
                }
            }.runTaskTimer(UntilTheEnd.getInstance(),0L,5L);
        }
    }
}
