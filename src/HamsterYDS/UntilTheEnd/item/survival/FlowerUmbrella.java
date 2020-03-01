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
public class FlowerUmbrella{
	public FlowerUmbrella() {	
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6伞"),1);
		materials.put(ItemManager.namesAndItems.get("§6猫尾"),1);
		materials.put(new ItemStack(Material.CHORUS_FLOWER),2);
		materials.put(new ItemStack(Material.YELLOW_FLOWER),2);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6花伞"),"§6生存");
		
		ChangeTasks.umbrellas.add("§6花伞");
	}
}
