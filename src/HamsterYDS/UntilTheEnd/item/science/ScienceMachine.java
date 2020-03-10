package HamsterYDS.UntilTheEnd.item.science;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.ItemManager;

public class ScienceMachine {
	public ScienceMachine() {		
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(new ItemStack(Material.LOG),4);
		materials.put(new ItemStack(Material.OBSIDIAN),4);
		materials.put(new ItemStack(Material.GOLD_INGOT),1);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6科学机器"),"§6科学");
		
		ItemManager.canPlaceBlocks.put("ScienceMachine",ItemManager.namesAndItems.get("§6科学机器"));
	}
}
