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
	public static double percent = ItemManager.yaml2.getDouble("草帽.percent");

	public StrawHat() {
		HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
		materials.put(new ItemStack(Material.LEAVES), 6);
		materials.put(ItemManager.namesAndItems.get("§6绳子"), 1);
		ItemManager.registerRecipe(materials, ItemManager.namesAndItems.get("§6草帽"), "§6衣物");

		ChangeTasks.clothesChangeTemperature.put("§6草帽", percent);
	}
}
