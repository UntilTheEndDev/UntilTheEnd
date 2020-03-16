package HamsterYDS.UntilTheEnd.item.clothes;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.cap.san.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Garland {
	public static int sanityImprove = ItemManager.itemAttributes.getInt("Garland.sanityImprove");

	public Garland() {
		HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
		materials.put(new ItemStack(Material.CHORUS_FLOWER), 1);
		materials.put(new ItemStack(Material.YELLOW_FLOWER), 1);
		ItemManager.items.get("").registerRecipe(materials, ItemManager.items.get("Garland"), "衣物");

		ChangeTasks.clothesChangeSanity.put("Garland", sanityImprove);
	}
}
