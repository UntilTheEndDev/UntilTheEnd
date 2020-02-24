package HamsterYDS.UntilTheEnd.item.basics;

import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.ItemProvider;

public class BlueGum {
	public static ItemStack item;
	public BlueGum() {		
		ItemProvider.addItem(this.getClass(),item);
	}
}
