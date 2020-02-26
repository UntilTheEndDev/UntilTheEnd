package HamsterYDS.UntilTheEnd.item.science;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.cap.hum.Humidity;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;
import HamsterYDS.UntilTheEnd.item.materials.Brick;
import HamsterYDS.UntilTheEnd.item.materials.NightMare;
import HamsterYDS.UntilTheEnd.item.materials.Plank;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Detector implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.detector");
	public Detector() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(4,NightMare.item.getType());
		recipe.addIngredient(3,Brick.item.getType());
		recipe.addIngredient(2,Plank.item.getType());
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		ItemStack item4=NightMare.item.clone();
		item4.setAmount(4);
		ItemStack item3=Brick.item.clone();
		item3.setAmount(3);
		ItemStack item2=Plank.item.clone();
		item2.setAmount(2);
		
		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		inv.setItem(14,item3);
		inv.setItem(15,item4);
		inv.setItem(16,item2);
		UntilTheEndApi.GuideApi.addCraftToItem(item,inv);
		UntilTheEndApi.GuideApi.addItemToCategory("§6科学",item);
	}
	
	@EventHandler public void onMove(PlayerMoveEvent event) {
		Player player=event.getPlayer();
		if(player.getItemInHand()==null) return;
		ItemStack item=player.getItemInHand().clone();
		item.setAmount(1);
		if(item.equals(this.item)) {
			if(Math.random()<=0.03) {
				int dist=0;
				player.sendTitle("§8§l您距离目的地§e"+String.valueOf(dist)+"格","");
				PlayerManager.change(player.getName(),"san",-5);
			}
		}
	}
}
