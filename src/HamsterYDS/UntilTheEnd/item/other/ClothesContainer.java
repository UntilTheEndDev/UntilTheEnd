package HamsterYDS.UntilTheEnd.item.other;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.Logging;
import HamsterYDS.UntilTheEnd.internal.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.internal.EventHelper;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class ClothesContainer implements Listener {
    public static HashMap<UUID, Inventory> invs = new HashMap<UUID, Inventory>();
    public static ArrayList<UUID> openers = new ArrayList<UUID>();

    public ClothesContainer() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            onJoin(new PlayerJoinEvent(player, null));
        }
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        if (!event.hasItem())
            return;
        if (EventHelper.isRight(event.getAction())) {
            ItemStack item = event.getItem();
            if (ItemManager.isSimilar(item, getClass())) {
                Inventory inv = invs.get(player.getUniqueId());
                player.openInventory(inv);
                openers.add(player.getUniqueId());
            }
        }
    }

    @EventHandler()
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (openers.contains(player.getUniqueId())) {
            openers.remove(player.getUniqueId());
            onQuit(new PlayerQuitEvent(player, null));
        }
    }

    @EventHandler()
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        File file = new File(ItemManager.plugin.getDataFolder(), "clothes/" + player.getUniqueId().toString() + ".yml");
        YamlConfiguration yaml = new YamlConfiguration();//YamlConfiguration.loadConfiguration(file);
        Inventory inv = invs.get(player.getUniqueId());
        for (int slot = 0; slot < inv.getSize(); slot++) {
            ItemStack item = inv.getItem(slot);
            if (item == null)
                continue;
            yaml.set(String.valueOf(slot), item);
        }
        try {
            yaml.save(file);
        } catch (IOException e) {
            Logging.getLogger().log(Level.SEVERE,
                    "Failed to save " + player.getName() + "'s clothes container!", e
            );
        }
    }

    @EventHandler()
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File file = new File(ItemManager.plugin.getDataFolder() + "/clothes", player.getUniqueId().toString() + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        Inventory inv = Bukkit.createInventory(player, 9, ItemManager.items.get("ClothesContainer").displayName);
        for (String path : yaml.getKeys(false)) {
            ItemStack item = yaml.getItemStack(path);
            inv.setItem(Integer.parseInt(path), item);
        }
        invs.put(player.getUniqueId(), inv);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (openers.contains(player.getUniqueId())) {
            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
                event.setCancelled(true);
            if (event.getClickedInventory() == null) return;
            if (event.getClickedInventory().getSize() != 9)
                return;
            ItemStack cursor = event.getCursor();
            if (cursor == null)
                return;
            if (ItemFactory.getType(cursor) == ItemFactory.fromLegacy(Material.AIR))
                return;
            String type = String.valueOf(ItemFactory.getType(cursor));
            if (type.contains("HELMET") || type.contains("CHESTPLATE") || type.contains("LEGGINGS")
                    || type.contains("BOOTS"))
                if (ItemManager.getUTEItemId(cursor, null) != null)
                    return;
            player.sendMessage("只有可穿戴的UTE衣物可以放入衣物管理器！");
            event.setCancelled(true);
        }
    }

    @EventHandler()
    public void onDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (openers.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    public static Inventory getInventory(Player player) {
        return invs.get(player.getUniqueId());
    }
}
