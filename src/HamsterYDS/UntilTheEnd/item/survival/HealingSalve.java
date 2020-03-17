package HamsterYDS.UntilTheEnd.item.survival;

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
public class HealingSalve implements Listener {
    public HealingSalve() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.items.get("SpiderGland"), 1);
        materials.put(ItemManager.items.get("Ashes"), 7);
        materials.put(new ItemStack(Material.BOWL), 1);
        ItemManager.items.get("").registerRecipe(materials, ItemManager.items.get("治疗药膏"), "生存");
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!EventHelper.isRight(event.getAction())) return;
        if (!event.hasItem()) return;
        ItemStack item = event.getItem().clone();
        if (item == null) return;
        item.setAmount(1);
        if (item.equals(ItemManager.items.get("治疗药膏"))) {
            event.setCancelled(true);
            if (!player.isSneaking()) return;
            ItemStack itemr = event.getItem();
            itemr.setAmount(itemr.getAmount() - 1);
            if (player.getHealth() + 10.0 >= player.getMaxHealth())
                player.setHealth(player.getMaxHealth());
            else
                player.setHealth(player.getHealth() + 10.0);
        }
    }
}
