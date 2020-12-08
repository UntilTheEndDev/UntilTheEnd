package ute.item.clothes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.inventory.ItemStack;
import ute.Config;
import ute.item.ItemManager;

public class BushesHat implements Listener {
    public BushesHat() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onTarget(EntityTargetEvent event) {
        if (!Config.enableWorlds.contains(event.getEntity().getWorld())) return;
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            ItemStack item = player.getInventory().getHelmet();
            if (ItemManager.isSimilar(item, getClass())){

                if (item.getDurability() >= item.getType().getMaxDurability())
                    item.setType(Material.AIR);
                if (Math.random() <= 0.03)
                    item.setDurability((short) (item.getDurability() + 1));

                if (event.getReason() == TargetReason.CLOSEST_PLAYER)
                    event.setCancelled(true);
            }
            ItemStack[] clothes = ClothesContainer.getInventory(player).getStorageContents();
            for (ItemStack cloth : clothes)
                if (ItemManager.isSimilar(cloth, getClass())) {

                    if (cloth.getDurability() >= cloth.getType().getMaxDurability())
                        cloth.setType(Material.AIR);
                    if (Math.random() <= 0.03)
                        cloth.setDurability((short) (cloth.getDurability() + 1));

                    if (event.getReason() == TargetReason.CLOSEST_PLAYER)
                        event.setCancelled(true);
                }
        }
    }
}
