package HamsterYDS.UntilTheEnd.item.materials;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class PurpleGum{
	public PurpleGum() {
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.items.get("RedGum"),1);
		materials.put(ItemManager.items.get("BlueGum"),1);
		ItemManager.items.get("").registerRecipe(materials,ItemManager.items.get("PurpleGum"),"基础");
	}
}
