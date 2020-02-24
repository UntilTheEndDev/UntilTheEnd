package HamsterYDS.UntilTheEnd.item.basics;

import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.ItemProvider;

public class AnimateWood {
	public static ItemStack item;
	public AnimateWood() {		
		ItemProvider.addItem(this.getClass(),item);
	}
}
