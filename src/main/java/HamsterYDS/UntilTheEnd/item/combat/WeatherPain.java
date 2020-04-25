package HamsterYDS.UntilTheEnd.item.combat;

import java.util.HashMap;
import java.util.UUID;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.Logging;
import HamsterYDS.UntilTheEnd.internal.DisableManager;
import HamsterYDS.UntilTheEnd.internal.EventHelper;
import HamsterYDS.UntilTheEnd.internal.ItemFactory;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import HamsterYDS.UntilTheEnd.item.ItemManager;

public class WeatherPain implements Listener {
    public static int dist = ItemManager.itemAttributes.getInt("WeatherPain.dist");
    public static double range = ItemManager.itemAttributes.getDouble("WeatherPain.range");

    public WeatherPain() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    private static final HashMap<UUID, Integer> cd = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        Logging.getLogger().fine(() -> "[WeatherPain] {action=" + event.getAction() + ", cancelled=" + event.isCancelled() + "}");
        if (event.isCancelled() && !DisableManager.bypass_right_action_cancelled) return;
        if (!event.hasItem()) return;
        if (!EventHelper.isRight(event.getAction())) return;
        ItemStack item = event.getItem();
        if (ItemManager.isSimilar(item, getClass())) {
            if (cd.containsKey(player.getUniqueId()))
                if (cd.get(player.getUniqueId()) > 0) {
                    player.sendMessage(UTEi18n.cacheWithPrefix("item.weather-pain.cd"));
                    return;
                }
            cd.put(player.getUniqueId(), 10);
            ItemStack itemr = event.getItem();
            itemr.setDurability((short) (itemr.getDurability() + 25));
            if (itemr.getDurability() > ItemFactory.getType(itemr).getMaxDurability())
                itemr.setType(Material.AIR);
            Location loc = player.getLocation().add(0.0, 1.0, 0.0);
            Vector vec = player.getEyeLocation().getDirection().multiply(0.5);
            player.setCooldown(ItemFactory.getType(item), 90);
            new BukkitRunnable() {
                int range = 150;

                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        range--;
                        if (range <= 0) {
                            cancel();
                            cd.remove(player.getUniqueId());
                            return;
                        }
                        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
                        loc.add(vec);
                        for (Entity entity : loc.getWorld().getNearbyEntities(loc, WeatherPain.range, WeatherPain.range, WeatherPain.range)) {
                            if (entity.getEntityId() == player.getEntityId()) continue;
                            for (int j = 0; j <= dist; j++) entity.setVelocity(vec);
                        }
                    }
                }
            }.runTaskTimer(ItemManager.plugin, 0L, 3L);
            event.setCancelled(true);
        }
    }
}
