package ute.item.materials;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ute.api.PlayerApi;
import ute.api.event.cap.TemperatureChangeEvent;
import ute.api.event.player.CustomItemInteractEvent;
import ute.item.ItemManager;

public class Hail implements Listener {
    public static int temperatureReduce = ItemManager.itemAttributes.getInt("Hail.temperatureReduce");

    public Hail() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(CustomItemInteractEvent event) {
        Player player=event.getWho();
        if (event.getUteItem().id.equalsIgnoreCase("Hail")) {
            event.setCancelled(true);
            PlayerApi.TemperatureOperations.changeTemperature(player, TemperatureChangeEvent.ChangeCause.ITEMS,temperatureReduce);
        }
    }
}
