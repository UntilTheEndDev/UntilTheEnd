package HamsterYDS.UntilTheEnd.cap.clothes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class GetHum {
	public static int getHum(Player player) {
		PlayerInventory inv=player.getInventory();
		return 30;
	}
	public void minusDura(PlayerInventory inv,int slot,ItemStack item,int durability) {
		item.setDurability((short) (item.getDurability()+durability));
		if(item.getDurability()>=item.getType().getMaxDurability()) inv.remove(slot);
		else inv.setItem(slot,item);
	}
}
