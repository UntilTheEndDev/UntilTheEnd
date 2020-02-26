package HamsterYDS.UntilTheEnd.item.basics;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.ItemProvider;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class PigSkin implements Listener{
	public static ItemStack item;
	public PigSkin() {		
		ItemProvider.addItem(this.getClass(),item);
	}
}
