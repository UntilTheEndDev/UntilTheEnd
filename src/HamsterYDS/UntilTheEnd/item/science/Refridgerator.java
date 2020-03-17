package HamsterYDS.UntilTheEnd.item.science;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.food.RottenFoodEvents;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Refridgerator{
	public Refridgerator() {		
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.items.get("Brick"),4);
		materials.put(ItemManager.items.get("电器元件"),3);
		materials.put(ItemManager.items.get("Gear"),2);
		ItemManager.items.get("").registerRecipe(materials,ItemManager.items.get("冰箱"),"科学");
		
		ItemManager.canPlaceBlocks.put("Refridgerator",ItemManager.items.get("冰箱"));
		RottenFoodEvents.titleFactors.put("冰箱",10);
	}
}
