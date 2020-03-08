package HamsterYDS.UntilTheEnd.item.survival;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.PlayerInventoryAdapt;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class MovablePack{
	public MovablePack() {	
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6兔毛"),5);
		materials.put(ItemManager.namesAndItems.get("§6牛毛"),3);
		materials.put(ItemManager.namesAndItems.get("§6背包"),1);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6便携包"),"§6生存");
		
		PlayerInventoryAdapt.containerSizes.put("§6便携包",4);
	}
}
