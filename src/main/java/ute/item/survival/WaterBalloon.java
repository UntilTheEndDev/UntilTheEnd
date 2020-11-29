package ute.item.survival;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ute.event.player.CustomItemInteractEvent;
import ute.item.ItemManager;
import ute.player.PlayerManager;

public class WaterBalloon implements Listener {
    public WaterBalloon() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(CustomItemInteractEvent event) {
        Player player=event.getWho();
        if (event.getUteItem().id.equalsIgnoreCase("WaterBalloon")) {
            event.setCancelled(true);
            PlayerManager.change(player, PlayerManager.CheckType.TEMPERATURE, -10);
            Location loc = player.getLocation();
            for (int x = -3; x <= 3; x++)
                for (int y = -3; y <= 3; y++)
                    for (int z = -3; z <= 3; z++) {
                        Block block = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z).getBlock();
                        if (block == null) continue;
                        if (block.getType() == Material.FIRE)
                            block.setType(Material.AIR);
                        if (block.isLiquid())
                            block.setType(Material.AIR);
                    }
        }
    }
}
