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
		materials.put(ItemManager.namesAndItems.get("§6绳子"),4);
		materials.put(ItemManager.namesAndItems.get("§6芦苇"),2);
		materials.put(ItemManager.namesAndItems.get("§6猪皮"),2);
		materials.put(new ItemStack(Material.STRING),1);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6伞"),"§6生存");
		
		ChangeTasks.umbrellas.add("§6伞");
	}
}
