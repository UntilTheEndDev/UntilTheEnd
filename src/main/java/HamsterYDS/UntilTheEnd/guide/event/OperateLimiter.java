package HamsterYDS.UntilTheEnd.guide.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.UUID;

public class OperateLimiter implements Listener {
    public static ArrayList<UUID> openers=new ArrayList<UUID>();
    @EventHandler(priority= EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event){
        if(openers.contains(event.getWhoClicked().getUniqueId())){
            event.setCancelled(true);
        }
    }
    @EventHandler(priority= EventPriority.LOWEST)
    public void onDrag(InventoryDragEvent event){
        if(openers.contains(event.getWhoClicked().getUniqueId())){
            event.setCancelled(true);
        }
    }
    @EventHandler(priority= EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event){
        openers.remove(event.getPlayer().getUniqueId());
    }
}
