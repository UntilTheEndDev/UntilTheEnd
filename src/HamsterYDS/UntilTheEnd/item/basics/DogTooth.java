package HamsterYDS.UntilTheEnd.item.basics;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.ItemProvider;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class DogTooth implements Listener{
	public static ItemStack item;
	public DogTooth() {		
		ItemProvider.addItem(this.getClass(),item);
	}
}
