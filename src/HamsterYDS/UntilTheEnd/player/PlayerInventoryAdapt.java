package HamsterYDS.UntilTheEnd.player;

import java.util.ArrayList;
import java.util.HashMap;

import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import HamsterYDS.UntilTheEnd.item.other.ClothesContainer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */

//20l
public class PlayerInventoryAdapt extends BukkitRunnable implements Listener {
    public static int lockingSlot = 18;
    public static HashMap<String, Integer> containerSizes = new HashMap<String, Integer>();

    public PlayerInventoryAdapt(UntilTheEnd plugin) {
        lockingSlot = plugin.getConfig().getInt("player.inventory.lockSlots");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.runTaskTimer(plugin, 0L, 20L);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!Config.enableWorlds.contains(event.getWhoClicked().getWorld())) return;
        Inventory inv = event.getClickedInventory();
        if (inv == null) return;
        ItemStack itemS = inv.getItem(event.getSlot());
        ItemStack itemC = event.getCursor();
        if (itemS != null)
            if (getName(itemS).equalsIgnoreCase(UTEi18n.cache("item.locked")))
                event.setCancelled(true);
        if (itemC != null)
            if (getName(itemC).equalsIgnoreCase(UTEi18n.cache("item.locked")))
                event.setCancelled(true);
    }

    private static ArrayList<String> lockingPlayers = new ArrayList<String>();
    private static int[] slots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 27, 28, 29, 30, 31, 32, 33, 34, 35, 18, 19, 20, 21, 22, 23, 24, 25, 26, 9, 10, 11, 12, 13, 14, 15, 16, 17};

    @Override
    public void run() {
        if (UntilTheEnd.getInstance().getConfig().getBoolean("player.inventory.enable"))
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)) continue;
                    if (player.getGameMode() == GameMode.CREATIVE) continue;
                    lockingPlayers.add(player.getName());
                    int extraSize = 0;
                    PlayerInventory inv = player.getInventory();
                    ItemStack[] clothes = ClothesContainer.getInventory(player).getStorageContents();
                    for (ItemStack cloth : clothes) 
                    	extraSize += getSize(cloth); 
                    for (ItemStack item : inv.getArmorContents())
                        extraSize += getSize(item);
                    for (int i = 35; i > 35 - lockingSlot + extraSize; i--) {
                        if (i < 0) break;
                        if (getName(inv.getItem(slots[i])).equalsIgnoreCase(UTEi18n.cache("item.locked")))
                            inv.getItem(slots[i]).setAmount(1);
                        if (inv.getItem(slots[i]) == null)
                            inv.setItem(slots[i], item1);
                    }
                    for (int i = 35 - lockingSlot + extraSize; i > -1; i--) {
                        if (i < 0) break;
                        ItemStack item = inv.getItem(slots[i]);
                        if (getName(item).equalsIgnoreCase(UTEi18n.cache("item.locked")))
                            inv.setItem(slots[i], new ItemStack(Material.AIR));
                    }
                }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (lockingPlayers.contains(player.getName())) continue;
            PlayerInventory inv = player.getInventory(); 
            for (int slot = 0; slot < inv.getSize(); slot++) {
                ItemStack item = inv.getItem(slot);
                if (getName(item).equalsIgnoreCase(UTEi18n.cache("item.locked")))
                    inv.remove(item);
            }
        }
        lockingPlayers = new ArrayList<String>();
    }

    public int getSize(ItemStack item) {
        if (containerSizes.containsKey(getName(item)))
            return containerSizes.get(getName(item));
        return 0;
    }

    public String getName(ItemStack item) {
        if (item == null) return "";
        if (item.hasItemMeta())
            if (item.getItemMeta().hasDisplayName())
                return item.getItemMeta().getDisplayName();
        return "";
    }

    private ItemStack item1 = getItem(Material.STAINED_GLASS_PANE, 15, UTEi18n.cache("item.locked"));

    public static ItemStack getItem(Material material, int data, String name) {
        ItemStack item = new ItemStack(material, 1);
        item.setDurability((short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
