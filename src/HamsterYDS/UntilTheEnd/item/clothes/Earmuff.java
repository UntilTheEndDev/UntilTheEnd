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
	public static double percent = ItemManager.itemAttributes.getDouble("兔毛耳罩.percent");

	public Earmuff() {
		HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
		materials.put(ItemManager.items.get("Rope"), 2);
		materials.put(ItemManager.items.get("RabbitFur"), 4);
		ItemManager.items.get("").registerRecipe(materials, ItemManager.items.get("兔毛耳罩"), "衣物");

		ChangeTasks.clothesChangeTemperature.put("兔毛耳罩", -percent);
	}
}
