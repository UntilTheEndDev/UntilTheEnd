package HamsterYDS.UntilTheEnd.item.clothes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.item.other.ClothesContainer;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BushesHat implements Listener {
    public BushesHat() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            // Entity entity = event.getEntity();
            ItemStack item = player.getInventory().getHelmet();
            if (ItemManager.isSimilar(item, getClass()))
                if (event.getReason() == TargetReason.CLOSEST_PLAYER)
                    event.setCancelled(true);
            ItemStack[] clothes = ClothesContainer.getInventory(player).getStorageContents();
            for (ItemStack cloth : clothes)
                if (ItemManager.isSimilar(cloth, getClass()))
                    if (event.getReason() == TargetReason.CLOSEST_PLAYER)
                        event.setCancelled(true);
        }
    }
}
