/**
 *
 */
package ute.api;

import ute.guide.craft.CraftGuide;
import ute.internal.ItemFactory;
import ute.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuideApi {
    public static void add_item_to_category(AvaliableCategories category, ItemStack item) {
        CraftGuide.button_to_gui.get(category.button).addItem(item);
    }

    public static void add_guide_to_item(ItemStack item, Inventory inventory) {
        ItemStack back = item_creater(UTEi18n.cache("item.guide.action.previous"), Material.STAINED_GLASS_PANE, (short) 6);
        ItemStack menu = item_creater(UTEi18n.cache("item.guide.action.main"), Material.STAINED_GLASS_PANE, (short) 9);
        inventory.setItem(0, back);
        inventory.setItem(8, menu);
        CraftGuide.button_to_gui.put(item, inventory);
    }

    public static ItemStack item_creater(String item_name, Material material, short data) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(item_name);
        item.setItemMeta(meta);
        item.setDurability(data);
        return item;
    }

    public static Inventory copy_inventory(Inventory inv,String title) {
        Inventory copy = Bukkit.createInventory(inv.getHolder(), inv.getSize(), title);
        copy.setContents(inv.getContents());
        return copy;
    }

    public enum AvaliableCategories {
        //这里带了LEGACY
        衣物(GuideApi.item_creater("§6衣物", ItemFactory.fromLegacy(Material.GOLD_HELMET), (short) 0), 14),
        战斗与工具(GuideApi.item_creater("§6战斗与工具", ItemFactory.fromLegacy(Material.DIAMOND_SWORD), (short) 0), 13),
        魔法(GuideApi.item_creater("§6魔法", ItemFactory.fromLegacy(Material.SPLASH_POTION), (short) 0), 15),
        精炼(GuideApi.item_creater("§6精炼", ItemFactory.fromLegacy(Material.LEASH), (short) 0), 10),
        科学(GuideApi.item_creater("§6科学", ItemFactory.fromLegacy(Material.REDSTONE_COMPARATOR), (short) 0), 12),
        生存(GuideApi.item_creater("§6生存", ItemFactory.fromLegacy(Material.IRON_PICKAXE), (short) 0), 11);
        public ItemStack button;
        public int slot_id;

        AvaliableCategories(ItemStack button, int slot_id) {
            this.button = button;
            this.slot_id = slot_id;
        }
    }
}