package HamsterYDS.UntilTheEnd.item.clothes;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.cap.hum.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class SwimmingSuit {
	public SwimmingSuit() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.items.get("Reed"), 3);
        materials.put(ItemManager.items.get("Rope"), 3);
        materials.put(ItemManager.items.get("CowHair"), 2);
        materials.put(ItemManager.items.get("RabbitFur"), 1);
        ItemManager.items.get("").registerRecipe(materials, ItemManager.items.get("防水服"), "衣物");
	
        ChangeTasks.waterProofSuits.add("防水服");
	}
}
