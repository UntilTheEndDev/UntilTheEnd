package ute.item.science;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import ute.event.player.CustomItemInteractEvent;
import ute.item.ItemManager;

public class CombinedTools implements Listener {
    public CombinedTools() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(CustomItemInteractEvent event) {
        Player player=event.getWho();
        if (event.getUteItem().id.equalsIgnoreCase("CombinedTools")) {
            ItemStack item=event.getItem();
            if (event.getClickedBlock()!=null) {
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
