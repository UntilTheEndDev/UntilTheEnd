package HamsterYDS.UntilTheEnd.cap.clothes;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class GetSan {
	public static int getSan(Player player) {
		PlayerInventory inv=player.getInventory();
		World world=player.getWorld();
		int fac=0;
		for(ItemStack item:inv.getArmorContents()) {
			if(item==null) continue;
			if(item.getItemMeta()==null) continue;
			if(item.getItemMeta().getDisplayName()==null) continue;
			String name=item.getItemMeta().getDisplayName();
			if(name.equalsIgnoreCase("§6花环")) {
				fac+=1;
				item.setDurability((short) (item.getDurability()+1));
				inv.setHelmet(item);
				if(item.getDurability()>=item.getType().getMaxDurability()) item=null;
			}
		}
		return fac;
	}
	public void minusDura(PlayerInventory inv,int slot,ItemStack item,int durability) {
		item.setDurability((short) (item.getDurability()+durability));
		if(item.getDurability()>=item.getType().getMaxDurability()) inv.remove(slot);
		else inv.setItem(slot,item);
	}
}
