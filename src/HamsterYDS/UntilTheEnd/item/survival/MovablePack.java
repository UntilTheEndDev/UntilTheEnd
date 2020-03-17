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
		materials.put(ItemManager.items.get("RabbitFur"),5);
		materials.put(ItemManager.items.get("CowHair"),3);
		materials.put(ItemManager.items.get("背包"),1);
		ItemManager.items.get("").registerRecipe(materials,ItemManager.items.get("便携包"),"生存");
		
		PlayerInventoryAdapt.containerSizes.put("便携包",4);
	}
}
