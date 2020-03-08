package HamsterYDS.UntilTheEnd.item.materials;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class PurpleGum{
	public PurpleGum() {
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6红宝石"),1);
		materials.put(ItemManager.namesAndItems.get("§6蓝宝石"),1);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6紫宝石"),"§6基础");
	}
}
