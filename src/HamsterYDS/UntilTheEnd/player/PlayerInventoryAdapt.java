package HamsterYDS.UntilTheEnd.player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */

public class PlayerInventoryAdapt extends BukkitRunnable implements Listener{
	public static UntilTheEnd plugin;
	ItemStack item1=getItem(Material.STAINED_GLASS_PANE,15,"§8锁定");
	public PlayerInventoryAdapt(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		runTaskTimer(plugin,0L,20L);
	}
	public static ItemStack getItem(Material material,int data,String name) {
		ItemStack item=new ItemStack(material,1);
		item.setDurability((short) data);
		ItemMeta meta=item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	public static String getName(ItemStack item) {
		if(item==null) return "";
		if(item.getItemMeta()==null) return "";
		if(item.getItemMeta().getDisplayName()==null) return "";
		return item.getItemMeta().getDisplayName();
	}
	@EventHandler public void onClick(InventoryClickEvent event) {
		Inventory inv=event.getClickedInventory();
		if(inv==null) return;
		ItemStack item1=inv.getItem(event.getSlot());
		ItemStack item2=event.getCursor();
		if(isLocked(item1)||isLocked(item2)) {
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler public void onMove(InventoryMoveItemEvent event) {
		ItemStack item=event.getItem();
		if(isLocked(item)) event.setCancelled(true);
	}
	@EventHandler public void onGet(InventoryDragEvent event) {
		ItemStack item=event.getOldCursor();
		if(isLocked(item)) event.setCancelled(true);
	}
	public boolean isLocked(ItemStack item) {
		if(getName(item).equalsIgnoreCase("§8锁定")) return true;
		return false;
	}
	@Override
	public void run() {
		for(Player player:Bukkit.getOnlinePlayers()) {
//			if(Config.disableWorlds.contains(player.getWorld().getName())) continue;
			PlayerInventory inv=player.getInventory();
//			if(player.hasPermission("ute.inventory.admin")) continue;
			int bag=getBag(inv.getChestplate());
			for(int i=26;i>26-bag;i--) {
				if(inv.getItem(i)==null) continue;
				if(isLocked(inv.getItem(i)))
					inv.setItem(i,null);
			}
			for(int i=26-bag;i>=9;i--){
				if(inv.getItem(i)!=null) 
					if(!inv.getItem(i).isSimilar(item1))
						continue;
				inv.setItem(i,item1); 
			}		
		}
	}
	public static int getBag(ItemStack item) {
		if(item==null) return 0;
		if(item.getItemMeta()==null) return 0;
		if(item.getItemMeta().getDisplayName()==null) return 0;
		if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§6便携包")) return 4;
		if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§6背包")) return 8;
		if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§6皮质背包")) return 12;
		return 0;
	}
}
