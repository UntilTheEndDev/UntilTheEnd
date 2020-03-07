package HamsterYDS.UntilTheEnd.item.clothes;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.cap.tem.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Earmuff {
	public static double percent = ItemManager.yaml2.getDouble("兔毛耳罩.percent");

	public Earmuff() {
		HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
		materials.put(ItemManager.namesAndItems.get("§6绳子"), 2);
		materials.put(ItemManager.namesAndItems.get("§6兔毛"), 4);
		ItemManager.registerRecipe(materials, ItemManager.namesAndItems.get("§6兔毛耳罩"), "§6衣物");

		ChangeTasks.clothesChangeTemperature.put("§6兔毛耳罩", -percent);
	}
}
