package HamsterYDS.UntilTheEnd.item.basics;

import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.ItemProvider;

public class RedGum {
	public static ItemStack item;
	public RedGum() {		
		ItemProvider.addItem(this.getClass(),item);
	}
}
