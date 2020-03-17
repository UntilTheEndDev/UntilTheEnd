package HamsterYDS.UntilTheEnd.item.survival;

import java.util.HashMap;

import HamsterYDS.UntilTheEnd.internal.EventHelper;
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
public class HoneyPoultice implements Listener {
    public HoneyPoultice() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.items.get("Spit"), 1);
        materials.put(ItemManager.items.get("Reed"), 1);
        materials.put(ItemManager.items.get("Ashes"), 7);
        ItemManager.items.get("").registerRecipe(materials, ItemManager.items.get("止血剂"), "生存");
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.hasItem()) return;
        if (!EventHelper.isRight(event.getAction())) return;
        ItemStack item = event.getItem().clone();
        if (item == null) return;
        item.setAmount(1);
        if (item.equals(ItemManager.items.get("止血剂"))) {
            event.setCancelled(true);
            if (!player.isSneaking()) return;
            ItemStack itemr = event.getItem();
            itemr.setAmount(itemr.getAmount() - 1);
            if (player.getHealth() + 12.0 >= player.getMaxHealth())
                player.setHealth(player.getMaxHealth());
            else
                player.setHealth(player.getHealth() + 12.0);
        }
    }
}
