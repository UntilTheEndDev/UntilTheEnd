/**
 *
 */
package ute.api;

import ute.guide.craft.CraftGuide;
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

    public static Inventory copy_inventory(Inventory inv) {
        Inventory copy = Bukkit.createInventory(inv.getHolder(), inv.getSize(), inv.getTitle());
        copy.setContents(inv.getContents());
        return copy;
    }

    public enum AvaliableCategories {
        衣物(GuideApi.item_creater("衣物", Material.GOLD_HELMET, (short) 0), 14),
        战斗(GuideApi.item_creater("战斗", Material.DIAMOND_SWORD, (short) 0), 13),
        魔法(GuideApi.item_creater("魔法", Material.SPLASH_POTION, (short) 0), 15),
        精炼(GuideApi.item_creater("精炼", Material.LEASH, (short) 0), 10),
        科学(GuideApi.item_creater("科学", Material.REDSTONE_COMPARATOR, (short) 0), 12),
        生存(GuideApi.item_creater("生存", Material.IRON_PICKAXE, (short) 0), 11);
        public ItemStack button;
        public int slot_id;

        AvaliableCategories(ItemStack button, int slot_id) {
            this.button = button;
            this.slot_id = slot_id;
        }
    }
}