package HamsterYDS.UntilTheEnd.cap.hum;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class InfluenceEvents implements Listener{ 
	public static UntilTheEnd plugin;
	public static double wetFoodLevel=Humidity.yaml.getDouble("wetFoodLevel");
	public InfluenceEvents(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	private ArrayList<String> wetFoodEaters=new ArrayList<String>();
	@EventHandler public void onUse(PlayerItemConsumeEvent event) {
		if(!Config.enableWorlds.contains(event.getPlayer().getWorld())) return;
		ItemStack item=event.getItem();
		if(!item.getType().isEdible()) return;
		if(isWet(item)) 
			wetFoodEaters.add(event.getPlayer().getName());
	}
	@EventHandler public void onEat(FoodLevelChangeEvent event) {
		Entity entity=event.getEntity();
		if(!Config.enableWorlds.contains(event.getEntity().getWorld())) return;
		if(wetFoodEaters.contains(entity.getName())) {
			event.setFoodLevel((int)(event.getFoodLevel()*wetFoodLevel));
			entity.sendMessage("§6[§cUntilTheEnd§6]§r 潮湿的食物真难吃~");  //Language-TODO
			wetFoodEaters.remove(entity.getName());
		}
	}
	@EventHandler public void onDrag(InventoryDragEvent event) {
		if(!Config.enableWorlds.contains(event.getWhoClicked().getWorld())) return;
		ItemStack item=event.getCursor();
		if(item==null) return;
		if(isWet(item)) {
			event.getWhoClicked().sendMessage("§6[§cUntilTheEnd§6]§r 潮湿的物品貌似不能拖动，它们太笨重了！"); //Language-TODO
			event.setCancelled(true);
		}
	}
	@EventHandler public void onClick(InventoryClickEvent event) {
		if(!Config.enableWorlds.contains(event.getWhoClicked().getWorld())) return;
		Inventory inv=event.getClickedInventory();
		if(inv==null) return;
		if(!(inv.getType()==InventoryType.WORKBENCH||inv.getType()==InventoryType.CRAFTING)) return;
		ItemStack item=event.getCursor();
		if(item==null) return;
		if(isWet(item)) {
			event.getWhoClicked().sendMessage("§6[§cUntilTheEnd§6]§r 潮湿的物品貌似不能用于合成，它们太笨重了！"); //Language-TODO
			event.setCancelled(true);
		}
	}
	public boolean isWet(ItemStack item) {
		if(item==null) return true;
		if(!item.hasItemMeta()) return false;
		if(!item.getItemMeta().hasLore()) return false;
		ItemMeta meta=item.getItemMeta();
		List<String> lores=meta.getLore();
		for(String s:lores) 
			if(s.contains("§8- §8§l潮湿的"))
				return true;
		return false;
	}
}
