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
	public static int sanityReduce=ItemManager.itemAttributes.getInt("Hail.sanityReduce");
	public NightMare() {	
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(new ItemStack(Material.GHAST_TEAR),4);
		ItemManager.items.get("").registerRecipe(materials,ItemManager.items.get("NightMare"),"基础");
		
		ChangeTasks.itemsChangeSanity.put("NightMare",sanityReduce);
	}
}
