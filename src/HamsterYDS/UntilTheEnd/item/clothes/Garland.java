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
	public static int sanityImprove = ItemManager.yaml2.getInt("花环.sanityImprove");

	public Garland() {
		HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
		materials.put(new ItemStack(Material.CHORUS_FLOWER), 1);
		materials.put(new ItemStack(Material.YELLOW_FLOWER), 1);
		ItemManager.registerRecipe(materials, ItemManager.namesAndItems.get("§6花环"), "§6衣物");

		ChangeTasks.clothesChangeSanity.put("§6花环", sanityImprove);
	}
}
