package HamsterYDS.UntilTheEnd.item.science;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import HamsterYDS.UntilTheEnd.api.BlockApi;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class LightningArrester implements Listener {
    public LightningArrester() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onLight(LightningStrikeEvent event) {
        Location loc = event.getLightning().getLocation();
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
