package HamsterYDS.UntilTheEnd.item.survival;

import java.util.HashMap;

import HamsterYDS.UntilTheEnd.internal.EventHelper;
import HamsterYDS.UntilTheEnd.internal.ItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;

import javax.lang.model.util.SimpleElementVisitor7;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class ACDDrug implements Listener {
    public ACDDrug() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.namesAndItems.get("§6止血剂"), 4);
        materials.put(ItemManager.namesAndItems.get("§6治疗药膏"), 4);
        materials.put(new ItemStack(Material.BOWL), 1);
        ItemManager.registerRecipe(materials, ItemManager.namesAndItems.get("§6ACD试验药"), "§6生存");
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
        if (ItemFactory.isSame(item, ItemManager.namesAndItems.get("§6ACD试验药"))) {
            event.setCancelled(true);
            if (!player.isSneaking()) return;
            ItemStack itemr = event.getItem();
            itemr.setAmount(itemr.getAmount() - 1);
            if (player.getMaxHealth() + 6.0 >= 40.0) player.setMaxHealth(40.0);
            else player.setMaxHealth(player.getMaxHealth() + 6.0);
        }
    }
}
