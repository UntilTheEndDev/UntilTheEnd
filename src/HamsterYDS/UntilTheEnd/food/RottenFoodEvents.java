package HamsterYDS.UntilTheEnd.food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

public class RottenFoodEvents implements Listener{
	public static HashMap<String,Double> titleFactors=new HashMap<String,Double>();
	@EventHandler public void onOpen(InventoryOpenEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getType()==InventoryType.PLAYER||
				inv.getType()==InventoryType.CRAFTING||
				inv.getType()==InventoryType.CREATIVE||
				inv.getType()==InventoryType.WORKBENCH) return;
		double factor=1;
		if(titleFactors.containsKey(inv.getTitle()))
			factor=titleFactors.get(inv.getTitle());
		for(int slot=0;slot<inv.getSize();slot++) {
			ItemStack item=inv.getItem(slot);
			if(item==null) return;
			if(item.getType()==Material.ROTTEN_FLESH) {
				clearTime(item);
				continue;
			}
			if(item.getType().isEdible()) {
				ItemMeta meta=item.getItemMeta();
				List<String> lores=new ArrayList<String>();
				if(meta!=null) {
					if(meta.hasLore()) {
						lores=meta.getLore();
						if(hasTag(lores)) return;
						long lastCloseTime=getTime(lores);
						long nowTime=System.currentTimeMillis();
						long rottenLevel=(long) ((nowTime-lastCloseTime)/1000/factor/UntilTheEnd.getInstance().getConfig().getInt("food.rotten.guispeed"));
						int currentLevel=RottenFoodTask.getRottenLevel(item);
						clearTime(item);
						if(rottenLevel>=currentLevel)  
							inv.setItem(slot,RottenFoodTask.setRottenLevel(item,-1));
						else 
							inv.setItem(slot,RottenFoodTask.setRottenLevel(item,(int) (currentLevel-rottenLevel)));
					}
				}
			}
		}
	}
	@EventHandler public void onClose(InventoryCloseEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getType()==InventoryType.PLAYER||
				inv.getType()==InventoryType.CRAFTING||
				inv.getType()==InventoryType.CREATIVE||
				inv.getType()==InventoryType.WORKBENCH) return;
		for(ItemStack item:inv.getContents()) {
			if(item==null) return;
			if(item.getType().isEdible()) {
				ItemMeta meta=item.getItemMeta();
				List<String> lores=new ArrayList<String>();
				if(meta!=null) 
					if(meta.hasLore()) 
						lores=meta.getLore();
				lores.add("上次打开时间："+System.currentTimeMillis());
				meta.setLore(lores);
				item.setItemMeta(meta);
			}
		}
	}
	private static boolean hasTag(List<String> lores) {
		for(String str:lores)
			if(str.contains("不可腐烂"))
				return true;
		return false;
	}
	private static long getTime(List<String> lores) {
		for(String str:lores)
			if(str.contains("上次打开时间：")) {
				String toString=str.replace("上次打开时间：","");
				return Long.valueOf(toString);
			}
		return System.currentTimeMillis();
	}
	private static void clearTime(ItemStack item) {
		ItemMeta meta=item.getItemMeta();
		List<String> lores=new ArrayList<String>();
		List<String> newLores=new ArrayList<String>();
		if(meta!=null) 
			if(meta.hasLore()) {
				for(String lore:lores) {
					if(!lore.contains("上次打开时间")) {
						newLores.add(lore);
					}
				}
			}
		meta.setLore(newLores);
		item.setItemMeta(meta);
	}
}
