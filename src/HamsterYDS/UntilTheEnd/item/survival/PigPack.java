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
		materials.put(ItemManager.items.get("PigSkin"),6);
		materials.put(ItemManager.items.get("背包"),1);
		materials.put(new ItemStack(Material.SHULKER_SHELL),2);
		ItemManager.items.get("").registerRecipe(materials,ItemManager.items.get("皮质背包"),"生存");
		
		PlayerInventoryAdapt.containerSizes.put("皮质背包",12);
	}
}
