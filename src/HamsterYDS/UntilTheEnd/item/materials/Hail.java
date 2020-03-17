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
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Hail implements Listener {
    public static int temperatureReduce = ItemManager.itemAttributes.getInt("Hail.temperatureReduce");

    public Hail() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(new ItemStack(Material.PACKED_ICE), 1);
        ItemManager.items.get("").registerRecipe(materials, ItemManager.items.get("Hail"), "基础");
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        if (!event.hasItem()) return;
        if (!EventHelper.isRight(event.getAction())) return;
        ItemStack item = event.getItem().clone();
        if (item == null) return;
        item.setAmount(1);
        if (item.equals(ItemManager.items.get("Hail"))) {
            ItemStack itemr = event.getItem();
            itemr.setAmount(itemr.getAmount() - 1);
            PlayerManager.change(player, PlayerManager.CheckType.TEMPERATURE, temperatureReduce);
        }
    }
}
