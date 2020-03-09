package HamsterYDS.UntilTheEnd.item.combat;

import java.util.HashMap;

import HamsterYDS.UntilTheEnd.internal.ArrowManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
    public static double damage = ItemManager.yaml2.getDouble("麻醉吹箭.damage");
    public static double range = ItemManager.yaml2.getDouble("麻醉吹箭.range");
    public static int maxDist = ItemManager.yaml2.getInt("麻醉吹箭.maxDist");
    public static int blindPeriod = ItemManager.yaml2.getInt("麻醉吹箭.blindPeriod");

    public BlowArrow3() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.namesAndItems.get("§6芦苇"), 3);
        materials.put(ItemManager.namesAndItems.get("§6狗牙"), 2);
        materials.put(ItemManager.namesAndItems.get("§6骨片"), 2);
        materials.put(ItemManager.namesAndItems.get("§6猫尾"), 1);
        ItemManager.registerRecipe(materials, ItemManager.namesAndItems.get("§6麻醉吹箭"), "§6战斗");
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking())
            return;
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (ItemManager.isSimilar(item, ItemManager.namesAndItems.get("§6麻醉吹箭"))) {
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
