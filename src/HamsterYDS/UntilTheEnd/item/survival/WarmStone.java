package HamsterYDS.UntilTheEnd.item.survival;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.cap.tem.TemperatureProvider;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class WarmStone implements Listener{
	public WarmStone() {
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.items.get("Rope"),4);
		materials.put(ItemManager.items.get("Brick"),4);
		materials.put(new ItemStack(Material.FURNACE),1);
		ItemManager.items.get("").registerRecipe(materials,ItemManager.items.get("暖石"),"生存");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
	}
	@EventHandler public void onThrow(PlayerDropItemEvent event) {
		ItemStack ritem=event.getItemDrop().getItemStack();
		ItemStack item=event.getItemDrop().getItemStack().clone();
		item.setAmount(1);
		if(item.equals(ItemManager.items.get("暖石"))) {
			ItemMeta meta=item.getItemMeta();
			List<String> lores=meta.getLore();
			for(String str:lores) {
				if(str.contains("§8- §8§l温度 ")) {
					lores.remove(str);
					break;
				}
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					lores.add("§8- §8§l温度 "+(int)TemperatureProvider.getBlockTemperature(event.getItemDrop().getLocation()));
					meta.setLore(lores);
					ritem.setItemMeta(meta);
					event.getItemDrop().setItemStack(ritem);
				}
			}.runTaskLater(ItemManager.plugin,20);
		}
	}
}
