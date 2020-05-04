package HamsterYDS.UntilTheEnd.item.science;

import HamsterYDS.UntilTheEnd.Config;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
public class CombinedTools implements Listener {
    public CombinedTools() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return;
        if (item.hasItemMeta())
            if (item.getItemMeta().hasDisplayName())
                if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.items.get("CombinedTools").displayName)) {
                   
                    if (event.hasBlock()) {
                        Block block = event.getClickedBlock();
                        if (block.getType().toString().contains("GRASS")
                                || block.getType().toString().contains("DIRT")
                                || block.getType().toString().contains("SAND")
                                || block.getType().toString().contains("GRAVEL")
                                || block.getType().toString().contains("CLAY")) {
                            item.setType(Material.DIAMOND_SPADE);
                            return;
                        }
                        if (block.getType().toString().contains("PLANK")
                                || block.getType().toString().contains("LOG")) {
                            item.setType(Material.DIAMOND_AXE);
                            return;
                        }
                        item.setType(Material.DIAMOND_PICKAXE);
                        return;
                    }
                    item.setType(Material.DIAMOND_SWORD);
                }
    }
}
