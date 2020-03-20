/**
 * 
 */
package HamsterYDS.UntilTheEnd.api;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;

/**
 * @author HamsterB君
 *
 */
public class GuideApi {
    public static void addItemToCategory(String category, ItemStack item) {
        CraftGuide.addItem("§6"+category, item);
    }

    public static void addCategory(String categoryName, Material material, short data) {
        ItemStack item = CraftGuide.getItem(categoryName, material, data);
        CraftGuide.inv.setItem(CraftGuide.tot++, item);
        CraftGuide.helps.put(categoryName, CraftGuide.getTypeInventory());
        CraftGuide.crafts.put(item, CraftGuide.helps.get(categoryName).get(0));
    }

    public static void addCraftToItem(ItemStack item, Inventory inventory) {
        ItemStack back = CraftGuide.getItem(UTEi18n.cache("item.guide.action.previous"), Material.STAINED_GLASS_PANE, 6);
        ItemStack menu = CraftGuide.getItem(UTEi18n.cache("item.guide.action.main"), Material.STAINED_GLASS_PANE, 9);
        inventory.setItem(0, back);
        inventory.setItem(8, menu);
        CraftGuide.crafts.put(item, inventory);
    }
}
