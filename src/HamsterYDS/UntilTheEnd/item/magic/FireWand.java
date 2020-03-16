package HamsterYDS.UntilTheEnd.item.magic;

import java.util.HashMap;

import HamsterYDS.UntilTheEnd.internal.EventHelper;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

public class FireWand implements Listener {
    public static int firePeriod = ItemManager.itemAttributes.getInt("旋风.firePeriod");
    public static int maxDist = ItemManager.itemAttributes.getInt("旋风.maxDist");
    public static double range = ItemManager.itemAttributes.getDouble("旋风.range");

    public FireWand() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.items.get("RedGum"), 3);
        materials.put(ItemManager.items.get("AnimateWood"), 3);
        materials.put(ItemManager.items.get("NightMare"), 1);
        ItemManager.items.get("").registerRecipe(materials, ItemManager.items.get("火魔杖"), "魔法");
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    private static HashMap<String, Integer> cd = new HashMap<String, Integer>();

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        if (!event.hasItem()) return;
        if (!EventHelper.isRight(event.getAction())) return;
        ItemStack itemClone = event.getItem().clone();
        if (itemClone == null) return;
        itemClone.setAmount(1);
        itemClone.setDurability((short) 0);
        if (itemClone.equals(ItemManager.items.get("火魔杖"))) {
            event.setCancelled(true);
            if (cd.containsKey(player.getName()))
                if (cd.get(player.getName()) > 0) {
                    player.sendMessage("[§cUntilTheEnd]§r 您的魔咒未冷却！");
                    return;
                }
            cd.remove(player.getName());
            cd.put(player.getName(), 10);
            ItemStack itemr = player.getItemInHand();
            itemr.setDurability((short) (itemr.getDurability() + 3));
            if (itemr.getDurability() > itemr.getType().getMaxDurability())
                player.setItemInHand(null);
            Location loc = player.getLocation().add(0.0, 1.0, 0.0);
            Vector vec = player.getEyeLocation().getDirection().multiply(0.5);
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
