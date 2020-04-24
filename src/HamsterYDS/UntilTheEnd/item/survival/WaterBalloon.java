package HamsterYDS.UntilTheEnd.item.survival;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.internal.DisableManager;
import HamsterYDS.UntilTheEnd.internal.EventHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

public class WaterBalloon implements Listener {
    public WaterBalloon() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        if (!event.hasItem()) return;
        if (event.isCancelled() && !DisableManager.bypass_right_action_cancelled) return;
        if (!EventHelper.isRight(event.getAction())) return;
        ItemStack item = event.getItem();
        if (ItemManager.isSimilar(item, getClass())) {
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
