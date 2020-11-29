package ute.item.science;

import ute.Config;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import ute.api.BlockApi;
import ute.item.ItemManager;

public class LightningArrester implements Listener {
    public LightningArrester() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onLight(LightningStrikeEvent event) {
        Location loc = event.getLightning().getLocation();
        if (!Config.enableWorlds.contains(loc.getWorld())) return;
        for (String str : BlockApi.getSpecialBlocks("LightningArrester")) {
            Location loc2 = BlockApi.strToLoc(str);
            if (loc.distance(loc2) <= 20) {
                event.setCancelled(true);
                loc.getWorld().strikeLightning(loc2.add(0.5, 0.5, 0.5));
                loc2.getWorld().spawnParticle(Particle.CRIT, loc2.add(0.0, 0.5, 0.0), 10);
                loc2.getWorld().spawnParticle(Particle.TOTEM, loc2.add(0.0, 0.5, 0.0), 10);
            }
        }
    }
}
