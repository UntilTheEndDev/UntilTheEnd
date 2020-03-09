package HamsterYDS.UntilTheEnd.guide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class MechanismGuide implements Listener {
    public static Inventory inv = Bukkit.createInventory(HolderMechanismGuide.INSTANCE, 45, UTEi18n.cache("item.guide.help.mechanism.main"));

    public MechanismGuide(UntilTheEnd plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        new MechanismGuide();
    }

    public MechanismGuide() {
        ItemStack frame = getItem1(UTEi18n.cache("item.guide.border"), Material.STAINED_GLASS_PANE, 15);
        for (int i = 0; i < 9; i++) inv.setItem(i, frame);
        inv.setItem(9, frame);
        inv.setItem(17, frame);
        inv.setItem(18, frame);
        inv.setItem(26, frame);
        inv.setItem(27, frame);
        inv.setItem(35, frame);
        for (int i = 36; i < 45; i++) inv.setItem(i, frame);

        ItemStack hud = getItem2(UTEi18n.cache("item.guide.mechanism.normal.name"), Material.STAINED_GLASS, 5, Arrays.asList(
                UTEi18n.cache("item.guide.mechanism.normal.lore").split("\\n")
        ));
        inv.setItem(19, hud);

        ItemStack sanity = getItem2(UTEi18n.cache("item.guide.mechanism.sanity.name"), Material.SKULL_ITEM, 3, Arrays.asList(
                UTEi18n.cache("item.guide.mechanism.sanity.lore").split("\\n")
        ));
        inv.setItem(21, sanity);

        ItemStack humidity = getItem2(UTEi18n.cache("item.guide.mechanism.humidity.name"), Material.WATER_BUCKET, 0, Arrays.asList(
                UTEi18n.cache("item.guide.mechanism.humidity.lore").split("\\n")
        ));
        inv.setItem(23, humidity);

        ItemStack temperature = getItem2(UTEi18n.cache("item.guide.mechanism.temperature.name"), Material.ICE, 0, Arrays.asList(
                UTEi18n.cache("item.guide.mechanism.temperature.lore").split("\\n")
        ));
        inv.setItem(25, temperature);
    }

    public static ArrayList<String> openers = new ArrayList<String>();

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getHolder() instanceof HolderMechanismGuide) openers.add(event.getPlayer().getName());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getHolder() instanceof HolderMechanismGuide) openers.remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (openers.contains(player.getName())) event.setCancelled(true);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (openers.contains(player.getName())) event.setCancelled(true);
    }

    private static ItemStack getItem1(String name, Material material, int data) {
        ItemStack item = new ItemStack(material);
        item.setDurability((short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getItem2(String name, Material material, int data, List<String> lores) {
        ItemStack item = new ItemStack(material);
        item.setDurability((short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;
    }
}
