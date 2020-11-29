package ute.item.science;

import ute.Config;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import ute.api.BlockApi;
import ute.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class IceFlingomatic implements Listener {
    public static int range = ItemManager.itemAttributes.getInt("IceFlingomatic.range");

    public IceFlingomatic() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBurn(BlockIgniteEvent event) {
        if (event.getIgnitingBlock() == null) return;
        if (!Config.enableWorlds.contains(event.getIgnitingBlock().getWorld())) return;
        for (String str : BlockApi.getSpecialBlocks("IceFlingomatic")) {
            Location loc = BlockApi.strToLoc(str);
            Location loc2 = event.getBlock().getLocation();
            assert loc != null;
            if (loc.distance(loc2) <= range) {
                loc2.getWorld().spawnParticle(Particle.SNOWBALL, loc2, 5);
                loc.getWorld().spawnParticle(Particle.SNOWBALL, loc.add(0.5, 1.0, 0.5), 5);
                event.setCancelled(true);
                return;
            }
        }
    }
}
