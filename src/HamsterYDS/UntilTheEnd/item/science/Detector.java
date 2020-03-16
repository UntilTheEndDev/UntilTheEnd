package HamsterYDS.UntilTheEnd.item.science;

import java.util.HashMap;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.cap.san.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Detector implements Listener{
	public Detector() {		
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.items.get("NightMare"),4);
		materials.put(ItemManager.items.get("Brick"),3);
		materials.put(ItemManager.items.get("Plank"),2);
		ItemManager.items.get("").registerRecipe(materials,ItemManager.items.get("探测器"),"科学");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
		
		ChangeTasks.itemsChangeSanity.put("探测器",-1);
	}
//	
//	@EventHandler public void onMove(PlayerMoveEvent event) {
//		Player player=event.getPlayer();
//		if(player.getItemInHand()==null) return;
//		ItemStack item=player.getItemInHand().clone();
//		item.setAmount(1);
//		if(item.equals(this.item)) {
//			if(Math.random()<=0.03) {
//				int dist=0;
//				player.sendTitle("§8§l您距离目的地§e"+String.valueOf(dist)+"格","");
//				PlayerManager.change(player,"san",-5);
//			}
//		}
//	}
}
