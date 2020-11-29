package ute.cap.hum;

import java.util.HashMap;
import java.util.UUID;

import ute.internal.ItemFactory;
import ute.internal.UTEi18n;
import ute.manager.WetManager;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ute.Config;
import ute.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class InfluenceEvents implements Listener {
    public static double wetFoodLevel = Humidity.yaml.getDouble("wetFoodLevel");

    public InfluenceEvents(UntilTheEnd plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private final HashMap<UUID, Integer> wetFoodLevels = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onUse(PlayerItemConsumeEvent event) {
        if (!Config.enableWorlds.contains(event.getPlayer().getWorld())) return;
        ItemStack item = event.getItem();
        if (!ItemFactory.getType(item).isEdible()) return;
        if (WetManager.isWet(item))
            wetFoodLevels.put(event.getPlayer().getUniqueId(), event.getPlayer().getFoodLevel());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEat(FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();
        if (!Config.enableWorlds.contains(event.getEntity().getWorld())) return;
        if (wetFoodLevels.containsKey(entity.getUniqueId())) {
            int currentLevel = wetFoodLevels.get(entity.getUniqueId());
            event.setFoodLevel((int) (((double) (event.getFoodLevel() - currentLevel)) * wetFoodLevel) + currentLevel);
            entity.sendMessage(UTEi18n.cacheWithPrefix("item.machine.wet.use"));
            wetFoodLevels.remove(entity.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDrag(InventoryDragEvent event) {
        if (!Config.enableWorlds.contains(event.getWhoClicked().getWorld())) return;
        ItemStack item = event.getCursor();
        if (item == null) return;
        if (WetManager.isWet(item)) {
            event.getWhoClicked().sendMessage(UTEi18n.cacheWithPrefix("item.machine.wet.inv-drag"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        if (!Config.enableWorlds.contains(event.getWhoClicked().getWorld())) return;
        Inventory inv = event.getClickedInventory();
        if (inv == null) return;
        if (!(inv.getType() == InventoryType.WORKBENCH || inv.getType() == InventoryType.CRAFTING)) return;
        ItemStack item = event.getCursor();
        if (item == null) return;
        if (WetManager.isWet(item)) {
            event.getWhoClicked().sendMessage(UTEi18n.cacheWithPrefix("item.machine.wet.no-crafting"));
            event.setCancelled(true);
        }
    }

}
