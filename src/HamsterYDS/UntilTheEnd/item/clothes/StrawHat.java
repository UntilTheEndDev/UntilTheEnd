package HamsterYDS.UntilTheEnd.item.clothes;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.cap.tem.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class StrawHat {
	public static double percent = ItemManager.itemAttributes.getDouble("草帽.percent");

	public StrawHat() {
		HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
		materials.put(new ItemStack(Material.LEAVES), 6);
		materials.put(ItemManager.items.get("Rope"), 1);
		ItemManager.items.get("").registerRecipe(materials, ItemManager.items.get("草帽"), "衣物");

		ChangeTasks.clothesChangeTemperature.put("草帽", percent);
	}
}
