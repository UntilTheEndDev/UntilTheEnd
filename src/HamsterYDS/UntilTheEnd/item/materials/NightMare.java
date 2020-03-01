package HamsterYDS.UntilTheEnd.item.materials;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.cap.san.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class NightMare{
	public NightMare() {	
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(new ItemStack(Material.GHAST_TEAR),4);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6噩梦燃料"),"§6基础");
		
		ChangeTasks.itemsChangeSanity.put("§6噩梦燃料",-1);
	}
}
