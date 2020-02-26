package HamsterYDS.UntilTheEnd.item.survival;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.cap.hum.Humidity;
import HamsterYDS.UntilTheEnd.cap.tem.TemperatureProvider;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;
import HamsterYDS.UntilTheEnd.item.materials.Brick;
import HamsterYDS.UntilTheEnd.item.materials.Rope;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class WarmStone implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.warmstone");
	public WarmStone() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(4,Rope.item.getType());
		recipe.addIngredient(4,Brick.item.getType());
		recipe.addIngredient(1,Material.FURNACE);
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		ItemStack item4=Rope.item.clone();
		item4.setAmount(4);
		ItemStack item4_2=Brick.item.clone();
		item4_2.setAmount(4);
		inv.setItem(14,item4);
		inv.setItem(15,item4_2);
		inv.setItem(16,new ItemStack(Material.FURNACE));
		UntilTheEndApi.GuideApi.addCraftToItem(item,inv);
		UntilTheEndApi.GuideApi.addItemToCategory("§6生存",item);
	}
	
	@EventHandler public void onCraft(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(Rope.item,4)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getInventory().containsAtLeast(Brick.item,4)) {
                event.setCancelled(true);
                return;
            }
        }
	}
	@EventHandler public void onThrow(PlayerDropItemEvent event) {
		ItemStack ritem=event.getItemDrop().getItemStack();
		ItemStack item=event.getItemDrop().getItemStack().clone();
		item.setAmount(1);
		if(item.getItemMeta()==null) return;
		if(item.getItemMeta().getDisplayName()==null) return;
		if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§6暖石")) {
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
					lores.add("§8- §8§l温度 "+TemperatureProvider.getBlockTemperature(event.getItemDrop().getLocation()));
					meta.setLore(lores);
					ritem.setItemMeta(meta);
					event.getItemDrop().setItemStack(ritem);
					cancel();
				}
			}.runTaskTimer(ItemLoader.plugin,20,1L);
		}
	}
}
