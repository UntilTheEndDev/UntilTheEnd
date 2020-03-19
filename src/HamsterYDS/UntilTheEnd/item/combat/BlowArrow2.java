package HamsterYDS.UntilTheEnd.item.combat;

import HamsterYDS.UntilTheEnd.internal.ArrowManager;
import HamsterYDS.UntilTheEnd.internal.EventHelper;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BlowArrow2 implements Listener {
    public static double damage = ItemManager.itemAttributes.getDouble("BlowArrow2.damage");
    public static double range = ItemManager.itemAttributes.getDouble("BlowArrow2.range");
    public static int maxDist = ItemManager.itemAttributes.getInt("BlowArrow2.maxDist");
    public static int firePeriod = ItemManager.itemAttributes.getInt("BlowArrow2.firePeriod");

    public BlowArrow2() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (EventHelper.isRight(event.getAction())) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (ItemManager.isSimilar(item, getClass())) {
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
}
