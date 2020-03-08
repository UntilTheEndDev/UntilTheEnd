package HamsterYDS.UntilTheEnd.item.materials;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Rope{
	public Rope() {
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(new ItemStack(Material.STRING),4); 
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6绳子"),"§6基础");
	}
}
