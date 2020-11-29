package ute.item.basics;

import ute.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import ute.item.ItemManager;

public class Ashes implements Listener {
    public static double percent = ItemManager.itemAttributes.getDouble("Ashes.percent");

    public Ashes() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBurn(BlockBurnEvent event) {
        if (event.isCancelled())
            return;
        if (!Config.enableWorlds.contains(event.getBlock().getWorld())) return;
        if (Math.random() <= percent) {
            event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), ItemManager.items.get("Ashes").item);
        }
    }
}
