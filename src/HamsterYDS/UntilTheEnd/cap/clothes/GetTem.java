package HamsterYDS.UntilTheEnd.cap.clothes;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import HamsterYDS.UntilTheEnd.cap.tem.NaturalTemperature;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class GetTem {
	public static int getTem(Player player) {
		PlayerInventory inv=player.getInventory();
		World world=player.getWorld();
		int fac=30;
		int index=0;
		for(ItemStack item:inv.getArmorContents()) {
			if(item==null) continue;
			if(item.getItemMeta()==null) continue;
			if(item.getItemMeta().getDisplayName()==null) continue;
//			int slot=36+slot;
			String name=item.getItemMeta().getDisplayName();
			int worldTem=NaturalTemperature.naturalTemperatures.get(world.getName());
			switch(name) {
				case "§6草帽":{
					if(worldTem>=40) {
						fac+=30;
						minusDura(inv,item,1);
					}
					break;
				}
				case "§6兔毛耳罩":{
					if(worldTem<=20) {
						fac+=30;
						minusDura(inv,item,1);
					}
					break;
				}
				default: fac+=0;
			}
			index++;
		}
		return fac;
	}
	public static void minusDura(PlayerInventory inv,ItemStack item,int durability) {
		if(Math.random()<=0.9) return;
		item.setDurability((short) (item.getDurability()+durability));
		if(item.getDurability()>=item.getType().getMaxDurability()) item=null;
//			inv.remove(slot);
//		else inv.setItem(slot,item);
	}
}
