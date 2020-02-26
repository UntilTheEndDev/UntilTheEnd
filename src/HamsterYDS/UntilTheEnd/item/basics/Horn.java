package HamsterYDS.UntilTheEnd.item.basics;

import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Horn implements Listener{
	public static ItemStack item;
	public Horn() {		
		ItemProvider.addItem(this.getClass(),item);
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
	}
	@EventHandler public void onRight(PlayerInteractEvent event) {
		Player player=event.getPlayer();
		if(!player.isSneaking()) return;
		if(event.getAction()!=Action.RIGHT_CLICK_AIR) return;
		ItemStack item=player.getItemInHand().clone();
		if(item==null) return;
		item.setAmount(1);
		if(item.equals(this.item)) {
			ItemStack itemr=player.getItemInHand();
			itemr.setAmount(itemr.getAmount()-1);
			
			for(Entity entity:player.getNearbyEntities(20.0,20.0,20.0)) 
				if(entity instanceof Cow) {
					Cow cow=(Cow) entity;
					System.out.println("Awa");
					cow.setAI(false);
					cow.setTarget(player);
					new BukkitRunnable() {
						@Override
						public void run() {
							cow.setTarget(null);
							cow.setAI(true);
						}
					}.runTaskTimer(ItemLoader.plugin,200,1);
				}
			if(player.getItemInHand().getAmount()==1) player.setItemInHand(null);
			else player.getItemInHand().setAmount(player.getItemInHand().getAmount()-1);
		}
	}
}
