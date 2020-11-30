package ute.item.magic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import ute.Config;
import ute.event.cap.SanityChangeEvent;
import ute.internal.DisableManager;
import ute.internal.EventHelper;
import ute.internal.ItemFactory;
import ute.item.ItemManager;
import ute.player.PlayerManager;

import java.util.HashMap;

public class FireWand implements Listener {
    public static int firePeriod = ItemManager.itemAttributes.getInt("FireWand.firePeriod");
    public static int maxDist = ItemManager.itemAttributes.getInt("FireWand.maxDist");
    public static double range = ItemManager.itemAttributes.getDouble("FireWand.range");

    public FireWand() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    private static HashMap<String, Integer> cd = new HashMap<String, Integer>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        if (event.isCancelled() && !DisableManager.bypass_right_action_cancelled) return;
        if (!event.hasItem()) return;
        if (!EventHelper.isRight(event.getAction())) return;
        ItemStack item = event.getItem();
        if (ItemManager.isSimilar(item, getClass())) {
            event.setCancelled(true);
            if (cd.containsKey(player.getName()))
                if (cd.get(player.getName()) > 0) {
                    player.sendMessage("§c[UntilTheEnd]§r 您的魔咒未冷却！");
                    return;
                }
            cd.remove(player.getName());
            cd.put(player.getName(), 5);
            ItemStack itemr = player.getItemInHand();
            itemr.setDurability((short) (itemr.getDurability() + 3));
            if (itemr.getDurability() > ItemFactory.getType(itemr).getMaxDurability())
                player.setItemInHand(null);
            Location loc = player.getLocation().add(0.0, 1.0, 0.0);
            Vector vec = player.getEyeLocation().getDirection().multiply(0.5);
            SanityChangeEvent event2 = new SanityChangeEvent(player, SanityChangeEvent.ChangeCause.USEWAND, -5);
            Bukkit.getPluginManager().callEvent(event2);
            if (!event2.isCancelled())
                PlayerManager.change(player, PlayerManager.CheckType.SANITY, -5);
            new BukkitRunnable() {
                int range = maxDist;

                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        range--;
                        if (range <= 0) {
                            cancel();
                            cd.remove(player.getName());
                            return;
                        }
                        loc.getWorld().spawnParticle(Particle.LAVA, loc, 1);
                        loc.add(vec);
                        for (Entity entity : loc.getWorld().getNearbyEntities(loc, FireWand.range, FireWand.range, FireWand.range)) {
                            if (entity.getEntityId() == player.getEntityId()) continue;
                            entity.setFireTicks(firePeriod * 20);
                            cancel();
                            cd.remove(player.getName());
                            return;
                        }
                    }
                }
            }.runTaskTimer(ItemManager.plugin, 0L, 1L);
        }
    }
}
