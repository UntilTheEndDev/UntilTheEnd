package ute.item.magic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ute.UntilTheEnd;
import ute.item.ItemManager;

public class Tracker implements Listener {

    public Tracker() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event){
        if(event.getEntity() instanceof Player){
            Player player= (Player) event.getEntity();
            Arrow arrow= (Arrow) event.getProjectile();
            ItemStack item=event.getBow();
            if(ItemManager.isSimilar(item,getClass())){
                new BukkitRunnable(){
                    int counter=0;
                    @Override
                    public void run() {
                        counter++;
                        if (counter >= 1200 || arrow.getLocation().getBlock().getType() != Material.AIR){
                            cancel();
                            return;
                        }
                        if(counter%5==0) {
                            Location loc = arrow.getLocation();
                            Entity target=null;
                            double minn=1e9;
                            for (Entity entity : arrow.getNearbyEntities(5, 5, 5)) {
                                if (entity.getUniqueId().equals(player.getUniqueId())) continue;
                                if (!(entity instanceof LivingEntity)) continue;
                                if(entity.getLocation().distance(arrow.getLocation())<=minn){
                                    minn=entity.getLocation().distance(arrow.getLocation());
                                    target=entity;
                                }
                            }
                            if(target==null) return;
                            Location entityLoc = target.getLocation().add(0,0.5,0);
                            Vector perfectDirection = loc.clone().subtract(entityLoc).toVector();
                            perfectDirection.normalize();
                            perfectDirection.multiply(-1);
                            arrow.setVelocity(perfectDirection.multiply(0.5));
                            return;
                        }
                    }
                }.runTaskTimer(UntilTheEnd.getInstance(),0L,1L);
            }
        }
    }
}
