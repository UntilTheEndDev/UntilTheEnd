package HamsterYDS.UntilTheEnd.item.science;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Refridgerator{
	public Refridgerator() {		
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6石砖"),4);
		materials.put(ItemManager.namesAndItems.get("§6电器元件"),3);
		materials.put(ItemManager.namesAndItems.get("§6齿轮"),2);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6冰箱"),"§6科学");
		
		ItemManager.canPlaceBlocks.put("Refridgerator",ItemManager.namesAndItems.get("§6冰箱"));
	}
}
