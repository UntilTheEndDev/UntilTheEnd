package HamsterYDS.UntilTheEnd.item.combat;

import HamsterYDS.UntilTheEnd.internal.ArrowManager;
import HamsterYDS.UntilTheEnd.internal.EventHelper;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BlowArrow1 implements Listener {
    public static double damage = ItemManager.itemAttributes.getDouble("吹箭.damage");
    public static double range = ItemManager.itemAttributes.getDouble("吹箭.range");
    public static int maxDist = ItemManager.itemAttributes.getInt("吹箭.maxDist");

    public BlowArrow1() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.items.get("Reed"), 3);
        materials.put(ItemManager.items.get("DogTooth"), 2);
        materials.put(ItemManager.items.get("Sclerite"), 2);
        materials.put(ItemManager.items.get("Scale"), 1);
        ItemManager.items.get("").registerRecipe(materials, ItemManager.items.get("吹箭"), "战斗");
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
        ItemManager.cosumeItems.add("BlowArrow1");
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking())
            return;
        if (!event.hasItem()) return;
        if (EventHelper.isRight(event.getAction())) {
            ItemStack item = event.getItem();
            if (ItemManager.isSimilar(item, ItemManager.items.get("吹箭"))) {
                event.setCancelled(true);
                ArrowManager.startFire(e -> e.damage(damage), null, new ItemStack(Material.IRON_SWORD),
                        player.getLocation().add(0, 1, 0), maxDist, ItemManager.plugin.getConfig().getInt("item.blowarrow.autoclear"),
                        player, range);
            }
        }
    }
}
