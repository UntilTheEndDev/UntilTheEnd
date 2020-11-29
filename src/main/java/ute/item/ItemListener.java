package ute.item;

import ute.Config;
import ute.internal.DisableManager;
import ute.internal.EventHelper;
import ute.internal.UTEi18n;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ItemListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
            return;
        if (!Config.enableWorlds.contains(event.getBlockPlaced().getWorld()))
            return;
        ItemStack item = event.getItemInHand();
        String id = ItemManager.getUTEItemId(item);
        if (!id.equalsIgnoreCase("")) {
            if (ItemManager.items.get(id).canPlace)
                return;
            event.setCancelled(true);
            event.getPlayer().sendMessage(UTEi18n.cacheWithPrefix("item.system.no-place"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onUse(PlayerInteractEvent event) {
        if (event.isCancelled() && !DisableManager.bypass_right_action_cancelled)
            return;
        if (!event.hasItem())
            return;
        if (EventHelper.isRight(event.getAction())) {
            Player player = event.getPlayer();
            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
                return;

            ItemStack item = event.getItem();
            String id = ItemManager.getUTEItemId(item);
            if (!id.equalsIgnoreCase("")) {
                if (ItemManager.items.get(id).isConsume) {
                    item.setAmount(item.getAmount() - 1);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material material = block.getType();
        if (ItemProvider.blockDrops.containsKey(material)) {
            HashMap<String, Double> drop = ItemProvider.blockDrops.get(material);
            if (dropItem(drop, block.getLocation()))
                event.setDropItems(false);
        }
    }

    public static boolean dropItem(HashMap<String, Double> drop, Location loc) {
        World world = loc.getWorld();
        boolean droped = false;
        for (String id : drop.keySet()) {
            ItemStack stack;
            if (ItemManager.items.containsKey(id))
                stack = ItemManager.items.get(id).item;
            else
                stack = new ItemStack(Material.valueOf(id));
            double percent = drop.get(id);
            while (percent >= 1.0) {
                droped = true;
                world.dropItemNaturally(loc, stack);
                percent--;
            }
            if (Math.random() <= percent) {
                droped = true;
                world.dropItemNaturally(loc, stack);
            }
        }
        return droped;
    }

    @EventHandler()
    public void onDeath(EntityDeathEvent event) {
        EntityType type = event.getEntityType();
        if (!ItemProvider.entityDrops.containsKey(type))
            return;
        HashMap<String, Double> drop = ItemProvider.entityDrops.get(type);
        dropItem(drop, event.getEntity().getLocation());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onUseAnvil(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (inv == null)
            return;
        ItemStack item = event.getCursor();
        if (inv instanceof AnvilInventory) {
            if (ItemManager.getUTEItemId(item, null) != null)
                event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDrop(ItemSpawnEvent event) {
        if (!ItemManager.plugin.getConfig().getBoolean("item.sawer.enable"))
            return;
        Item entityItem = event.getEntity();
        ItemStack item = entityItem.getItemStack();
        if (item.hasItemMeta())
            if (item.getItemMeta().hasDisplayName()) {
                entityItem.setCustomName(item.getItemMeta().getDisplayName());
                entityItem.setCustomNameVisible(true);
            }
    }
}