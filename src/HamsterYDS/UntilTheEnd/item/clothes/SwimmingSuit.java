package HamsterYDS.UntilTheEnd.item.clothes;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.cap.hum.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class SwimmingSuit {
	public SwimmingSuit() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.namesAndItems.get("§6芦苇"), 3);
        materials.put(ItemManager.namesAndItems.get("§6绳子"), 3);
        materials.put(ItemManager.namesAndItems.get("§6牛毛"), 2);
        materials.put(ItemManager.namesAndItems.get("§6兔毛"), 1);
        ItemManager.registerRecipe(materials, ItemManager.namesAndItems.get("§6防水服"), "§6衣物");
	
        ChangeTasks.waterProofSuits.add("§6防水服");
	}
}
