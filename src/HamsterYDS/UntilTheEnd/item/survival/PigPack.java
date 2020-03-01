package HamsterYDS.UntilTheEnd.item.survival;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.PlayerInventoryAdapt;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class PigPack{
	public PigPack() {	
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6猪皮"),6);
		materials.put(ItemManager.namesAndItems.get("§6背包"),1);
		materials.put(new ItemStack(Material.SHULKER_SHELL),2);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6皮质背包"),"§6生存");
		
		PlayerInventoryAdapt.containerSizes.put("§6皮质背包",12);
	}
}
