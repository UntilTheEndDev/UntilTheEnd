package HamsterYDS.UntilTheEnd.item.materials;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Hail implements Listener{
	public static int temperatureReduce=ItemManager.yaml.getInt("冰雹.temperatureReduce");
	public Hail() {		
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(new ItemStack(Material.PACKED_ICE),1);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6冰雹"),"§6基础");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
	}
	@EventHandler public void onRight(PlayerInteractEvent event) {
		Player player=event.getPlayer();
		if(!player.isSneaking()) return;
		if(event.getAction()!=Action.RIGHT_CLICK_AIR) return;
		ItemStack item=player.getItemInHand().clone();
		if(item==null) return;
		item.setAmount(1);
		if(item.equals(ItemManager.namesAndItems.get("§6冰雹"))) {
			ItemStack itemr=player.getItemInHand();
			itemr.setAmount(itemr.getAmount()-1);
			PlayerManager.change(player.getName(),"tem",temperatureReduce);
		}
	}
}
