package HamsterYDS.UntilTheEnd.item.materials;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Coin{
	public Coin() {		
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(new ItemStack(Material.GOLD_NUGGET),8);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6金币"),"§6基础");
	}
}
