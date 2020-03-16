package HamsterYDS.UntilTheEnd.item.materials;

import java.util.HashMap;

import HamsterYDS.UntilTheEnd.internal.EventHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Fern implements Listener {
    public static double heal = ItemManager.itemAttributes.getDouble("Fern.heal");

    public Fern() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(new ItemStack(Material.SEEDS), 6);
        ItemManager.items.get("").registerRecipe(materials, ItemManager.items.get("Fern"), "基础");
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        if (!EventHelper.isRight(event.getAction())) return;
        if (!event.hasItem()) return;
        ItemStack itemClone = event.getItem().clone();
        if (itemClone == null) return;
        itemClone.setAmount(1);
        if (itemClone.equals(ItemManager.items.get("Fern"))) {
            ItemStack item = event.getItem();
            item.setAmount(item.getAmount() - 1);
            if (player.getHealth() + heal >= player.getMaxHealth()) player.setHealth(player.getMaxHealth());
            else player.setHealth(player.getHealth() + heal);
        }
    }
}
