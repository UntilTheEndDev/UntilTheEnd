package HamsterYDS.UntilTheEnd.item.science;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Element{
	public Element() {		
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6石砖"),6);
		materials.put(ItemManager.namesAndItems.get("§6冰雹"),1);
		materials.put(new ItemStack(Material.GOLD_INGOT),2);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6电器元件"),"§6科学");
	}	
}
