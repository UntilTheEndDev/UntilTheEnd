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
public class NormalPack{
	public NormalPack() {	
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6绳子"),4);
		materials.put(new ItemStack(Material.LOG),4);
		materials.put(new ItemStack(Material.CHEST),1);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6背包"),"§6生存");
		
		PlayerInventoryAdapt.containerSizes.put("§6背包",8);
	}
}
