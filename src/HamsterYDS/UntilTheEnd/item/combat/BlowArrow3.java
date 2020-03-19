package HamsterYDS.UntilTheEnd.item.combat;

import HamsterYDS.UntilTheEnd.internal.ArrowManager;
import HamsterYDS.UntilTheEnd.internal.EventHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BlowArrow3 implements Listener {
    public static double damage = ItemManager.itemAttributes.getDouble("BlowArrow3.damage");
    public static double range = ItemManager.itemAttributes.getDouble("BlowArrow3.range");
    public static int maxDist = ItemManager.itemAttributes.getInt("BlowArrow3.maxDist");
    public static int blindPeriod = ItemManager.itemAttributes.getInt("BlowArrow3.blindPeriod");

    public BlowArrow3() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.hasItem()) return;
        if (EventHelper.isRight(event.getAction())) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (ItemManager.isSimilar(item, getClass())) {
                event.setCancelled(true);
                ArrowManager.startFire(le -> {
                            le.damage(damage);
                            le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, blindPeriod * 20, 0));
                            le.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, blindPeriod * 20, 0));
                        }, null, new ItemStack(Material.STONE_SWORD),
                        player.getLocation().add(0, 1, 0),
                        maxDist,
                        ItemManager.plugin.getConfig().getInt("item.blowarrow.autoclear"),
                        player, range);
            }
        }
    }
}
