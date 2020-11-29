package ute.item.combat;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import ute.event.player.CustomItemInteractEvent;
import ute.internal.ArrowManager;
import ute.item.ItemManager;

public class BlowArrow2 implements Listener {
    public static double damage = ItemManager.itemAttributes.getDouble("BlowArrow2.damage");
    public static double range = ItemManager.itemAttributes.getDouble("BlowArrow2.range");
    public static int maxDist = ItemManager.itemAttributes.getInt("BlowArrow2.maxDist");
    public static int firePeriod = ItemManager.itemAttributes.getInt("BlowArrow2.firePeriod");

    public BlowArrow2() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(CustomItemInteractEvent event) {
        Player player=event.getWho();
        if (event.getUteItem().id.equalsIgnoreCase("BlowArrow2")) {
            event.setCancelled(true);
            ArrowManager.startFire(e -> {
                        e.damage(damage);
                        e.setFireTicks(firePeriod * 20);
                    }, null, new ItemStack(Material.GOLD_SWORD),
                    player.getLocation().add(0, 1, 0),
                    maxDist,
                    ItemManager.plugin.getConfig().getInt("item.blowarrow.autoclear"),
                    player, range);
        }
    }
}
