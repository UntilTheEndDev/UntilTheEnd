package HamsterYDS.UntilTheEnd.item.science;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.cap.hum.Humidity;
import HamsterYDS.UntilTheEnd.cap.tem.TemperatureProvider;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;
import HamsterYDS.UntilTheEnd.item.materials.Brick;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Thermometer implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.thermometer");
	public Thermometer() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(4,Brick.item.getType());
		recipe.addIngredient(3,Element.item.getType());
		recipe.addIngredient(2,Material.GOLD_INGOT);
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		ItemStack item4=Brick.item.clone();
		item4.setAmount(4);
		inv.setItem(14,item4);
		ItemStack item3=Element.item.clone();
		item3.setAmount(3);
		inv.setItem(15,item3);
		inv.setItem(16,new ItemStack(Material.GOLD_INGOT,2));
		UntilTheEndApi.GuideApi.addCraftToItem(item,inv);
		UntilTheEndApi.GuideApi.addItemToCategory("§6科学",item);
		
		ItemLoader.canPlace.put("§6温度计","Thermometer");
	}
	
	@EventHandler public void onCraft1(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(Brick.item,4)) {
                event.setCancelled(true);
            }
            if (!event.getInventory().containsAtLeast(Element.item,3)) {
                event.setCancelled(true);
            }
        }
	}
	ArrayList<String> clicked=new ArrayList<String>();
	@EventHandler 
	public void onClick(PlayerInteractEvent event) {
		if(event.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
		Player player=event.getPlayer();
		Block block=event.getClickedBlock();
		Location loc=block.getLocation();
		String toString=BlockApi.locToStr(loc);
		if(UntilTheEndApi.BlockApi.getSpecialBlocks("Thermometer").contains(toString)) {
			if(clicked.contains(toString)) return;
			clicked.add(toString);
			int tem=TemperatureProvider.getBlockTemperature(loc);
			String text="§e§l温度§d§l"+tem+"§e§l°C";
			System.out.println(text);
			final Hologram hologram=HologramsAPI.createHologram(ItemLoader.plugin,loc.add(0.5,2.2,0.5).clone());
			hologram.appendTextLine(text);
			if(tem>=55)
				hologram.appendItemLine(new ItemStack(Material.BLAZE_POWDER));
			if(tem<=15)
				hologram.appendItemLine(new ItemStack(Material.ICE));
			new BukkitRunnable() {
				@Override
				public void run() {
					hologram.delete();
					clicked.remove(toString);
					cancel();
				}
			}.runTaskTimer(ItemLoader.plugin,200,1);
		}
	}
}
