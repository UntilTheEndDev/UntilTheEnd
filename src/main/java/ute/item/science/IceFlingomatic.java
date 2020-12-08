package ute.item.science;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.util.Vector;
import ute.Config;
import ute.api.BlockApi;
import ute.item.ItemManager;

public class IceFlingomatic implements Listener {
    public static int range = ItemManager.itemAttributes.getInt("IceFlingomatic.range");

    public IceFlingomatic() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onBurn(BlockIgniteEvent event) {
        if (event.getBlock() == null) return;
        if (!Config.enableWorlds.contains(event.getBlock().getWorld())) return;
        Location igniteLoc = event.getBlock().getLocation();
        for (String str : BlockApi.getSpecialBlocks("IceFlingomatic")) {
            Location loc = BlockApi.strToLoc(str);
            if (loc.distance(igniteLoc) <= range) {
                buildLine(loc,igniteLoc);
                event.setCancelled(true);
            }
        }
    }

    public static void buildLine(Location locA, Location locB) {
        Vector vectorAB = locB.clone().subtract(locA).toVector();
        double vectorLength = vectorAB.length();
        vectorAB.normalize();
        for (double i = 0; i < vectorLength; i += 0.1) {
            Vector vector = vectorAB.clone().multiply(i);
            locA.add(vector);
            locA.getWorld().spawnParticle(Particle.SNOWBALL, locA.clone().add(0.5, 1.0, 0.5), 1);
            locA.subtract(vector);
        }
    }
}
