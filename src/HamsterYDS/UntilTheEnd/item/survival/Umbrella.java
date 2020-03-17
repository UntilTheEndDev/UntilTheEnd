package HamsterYDS.UntilTheEnd.item.survival;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.cap.hum.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Umbrella{
	public Umbrella() {		
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.items.get("Rope"),4);
		materials.put(ItemManager.items.get("Reed"),2);
		materials.put(ItemManager.items.get("PigSkin"),2);
		materials.put(new ItemStack(Material.STRING),1);
		ItemManager.items.get("").registerRecipe(materials,ItemManager.items.get("伞"),"生存");
		
		ChangeTasks.umbrellas.add("伞");
	}
}
