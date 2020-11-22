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

import java.util.HashMap;
import java.util.UUID;

public class Wendy implements Listener {
    public static HashMap<UUID,Long> ghostLastSummonedStamp = new HashMap<UUID,Long>();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(EntityDamageEvent event){
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
            Location spawnLoc=player.getLocation();
            Rabbit ghost= (Rabbit) player.getWorld().spawnEntity(spawnLoc, EntityType.RABBIT);
            ghost.setCustomName("§6§lAbigail");
            ghost.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,1000000,0,false,false));
            ghost.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,1000000,0,false,false));
            ghost.setAI(false);
            ghost.setCustomNameVisible(true);
            player.sendTitle("§6§l鬼魂已召唤","");
            new BukkitRunnable(){
                Entity target=player;
                double distance=100.0;
                @Override
                public void run() {
                    if(ghost.isDead()){
                        cancel();
                    }
                    for(Entity entity:player.getNearbyEntities(10,10,10)){
                        if(entity instanceof LivingEntity) {
                            if (player.getLocation().distance(entity.getLocation()) < distance) {
                                target = entity;
                                distance = player.getLocation().distance(entity.getLocation());
                            }
                        }
                    }
                    ghost.setTarget((LivingEntity) target);
                }
            }.runTaskTimer(UntilTheEnd.getInstance(),0L,10L);
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(ghost.isDead()){
                        cancel();
                    }
                    for(Entity entity:player.getNearbyEntities(1,1,1)){
                        if(entity instanceof LivingEntity && entity.getUniqueId()!=player.getUniqueId()) {
                            //TODO 数据配置：鬼魂伤害5.0
                            ((LivingEntity) entity).damage(5.0);
                        }
                    }
                }
            }.runTaskTimer(UntilTheEnd.getInstance(),0L,20L);
            new BukkitRunnable(){
                @Override
                public void run() {
                    ghost.remove();
                }
                //TODO 存在时间 config
            }.runTaskLater(UntilTheEnd.getInstance(),1200L);
            new BukkitRunnable(){
                @Override
                public void run() {
                   Location loc=ghost.getLocation();
                   if(loc.getWorld().getUID()!=player.getWorld().getUID()){
                       ghost.teleport(player.getLocation());
                   }
                    if(loc.distance(player.getLocation())>=20.0){
                        ghost.teleport(player.getLocation());
                    }
                    ghost.getWorld().spawnParticle(Particle.SMOKE_LARGE,loc,10);
                    ghost.getWorld().spawnParticle(Particle.SMOKE_LARGE,loc.add(0,0.5,0),10);
                    ghost.getWorld().spawnParticle(Particle.SMOKE_LARGE,loc.add(0,1,0),10);
                }
            }.runTaskTimer(UntilTheEnd.getInstance(),0L,5L);
        }
    }
}
