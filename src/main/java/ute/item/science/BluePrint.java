package ute.item.science;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import ute.item.ItemManager;
import ute.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BluePrint implements Listener {
    public BluePrint() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player=event.getPlayer();
        ItemStack item=player.getInventory().getItemInMainHand();
        if(item==null) return;
        if(item.hasItemMeta())
        	if(item.getItemMeta().hasDisplayName())
        		if(item.getItemMeta().getDisplayName().contains(ItemManager.items.get("BluePrint").displayName)) {
        			String name=item.getItemMeta().getDisplayName();
        			String recipeName=name.replace(ItemManager.items.get("BluePrint").displayName,"");
        			String id=ItemManager.ids.get(recipeName);
        			if(PlayerManager.checkUnLockedRecipes(player).contains(id))
        				event.setCancelled(true);
        			else{
        				PlayerManager.addUnLockedRecipes(player,id);
        				Location location=player.getLocation();
        				for (double i = 0; i < 180; i += 180 / 6) {
        				    double radians = Math.toRadians(i)*2;
        				    double radius = Math.sin(radians);
        				    double y = Math.cos(radians);
        				    for (double j = 0; j < 360; j += 180 / 6) {
        				        double radiansCircle = Math.toRadians(j);
        				        double x = Math.cos(radiansCircle) * radius;
        				        double z = Math.sin(radiansCircle) * radius;
        				        location.add(x, y, z);
        				        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 1);
        				        location.subtract(x, y, z);
        				    }
        				}
        			}
        			item.setAmount(item.getAmount()-1); 
        			player.getInventory().setItemInMainHand(item);
        		}
    }
}
