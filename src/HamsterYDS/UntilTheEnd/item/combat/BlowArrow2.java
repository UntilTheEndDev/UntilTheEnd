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
import java.util.HashMap;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BlowArrow2 implements Listener {
    public static double damage = ItemManager.yaml2.getDouble("火吹箭.damage");
    public static double range = ItemManager.yaml2.getDouble("火吹箭.range");
    public static int maxDist = ItemManager.yaml2.getInt("火吹箭.maxDist");
    public static int firePeriod = ItemManager.yaml2.getInt("火吹箭.firePeriod");

    public BlowArrow2() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.namesAndItems.get("§6芦苇"), 3);
        materials.put(ItemManager.namesAndItems.get("§6狗牙"), 2);
        materials.put(ItemManager.namesAndItems.get("§6骨片"), 2);
        materials.put(ItemManager.namesAndItems.get("§6暖石"), 1);
        ItemManager.registerRecipe(materials, ItemManager.namesAndItems.get("§6火吹箭"), "§6战斗");
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
        ItemManager.cosumeItems.add("BlowArrow1");
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking())
            return;
        if (EventHelper.isRight(event.getAction())) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (ItemManager.isSimilar(item, ItemManager.namesAndItems.get("§6火吹箭"))) {
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
