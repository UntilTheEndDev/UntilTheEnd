package HamsterYDS.UntilTheEnd.guide;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Guide implements Listener{
	public static UntilTheEnd plugin;
	public static ItemStack item=new ItemStack(Material.ENCHANTED_BOOK);
	public static ItemStack craftItem=new ItemStack(Material.ENCHANTED_BOOK);
	public static ItemStack mechanismItem=new ItemStack(Material.ENCHANTED_BOOK);
	public static Inventory inv=Bukkit.createInventory(null,27,"UntilTheEnd:帮助");
	
	public Guide(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		new CraftGuide(plugin);
		new MechanismGuide(plugin);
		ItemMeta meta1=item.getItemMeta();
		meta1.setDisplayName("§6§lUntilTheEnd指导");
		item.setItemMeta(meta1);
		ItemMeta meta2=craftItem.getItemMeta();
		meta2.setDisplayName("§6§lUntilTheEnd合成指导");
		craftItem.setItemMeta(meta2);
		ItemMeta meta3=mechanismItem.getItemMeta();
		meta3.setDisplayName("§6§lUntilTheEnd机制指导");
		mechanismItem.setItemMeta(meta3);
		
		ItemStack frame=getItem("§8边框",Material.STAINED_GLASS_PANE,15);
		for(int i=0;i<9;i++) inv.setItem(i,frame);
		inv.setItem(9,frame);inv.setItem(17,frame);
		for(int i=18;i<27;i++) inv.setItem(i,frame);
		inv.setItem(10,craftItem);
		inv.setItem(11,mechanismItem);
	}
	@EventHandler public void onClick(InventoryClickEvent event) {
		Player player=(Player) event.getWhoClicked();
		Inventory inv=event.getClickedInventory();
		if(inv==null) return;
		if(openers.contains(player.getName())) 
			if(event.getAction()==InventoryAction.MOVE_TO_OTHER_INVENTORY)
				event.setCancelled(true); 
		if(!inv.getName().equalsIgnoreCase("UntilTheEnd:帮助")) return;
		ItemStack item=event.getInventory().getItem(event.getSlot());
		event.setCancelled(true);
		if(item==null) return;
		if(item.equals(craftItem)) {
			if(CraftGuide.playerInvs.get(player.getName()).size()==0) player.openInventory(CraftGuide.inv);
			else {
				player.openInventory(CraftGuide.playerInvs.get(player.getName()).get(CraftGuide.playerInvs.get(player.getName()).size()-1));
				CraftGuide.playerInvs.get(player.getName()).remove(CraftGuide.playerInvs.get(player.getName()).size()-1);
			}
		}
		if(item.equals(mechanismItem)) 
			player.openInventory(MechanismGuide.inv);
	}
	public static ArrayList<String> openers=new ArrayList<String>();
	@EventHandler public void onOpen(InventoryOpenEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getName().equalsIgnoreCase("UntilTheEnd:帮助")) openers.add(event.getPlayer().getName());
	}
	@EventHandler public void onClose(InventoryCloseEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getName().equalsIgnoreCase("UntilTheEnd:帮助")) openers.remove(event.getPlayer().getName());
	}
	@EventHandler public void onDrag(InventoryDragEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getName().equalsIgnoreCase("UntilTheEnd:帮助")) event.setCancelled(true);
	}
	private static ItemStack getItem(String name,Material material,int data) {
		ItemStack item=new ItemStack(material);
		item.setDurability((short) data);
		ItemMeta meta=item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	@EventHandler public void onRight(PlayerInteractEvent event) {
		Player player=event.getPlayer();
		if(event.getAction()==Action.RIGHT_CLICK_BLOCK||event.getAction()==Action.RIGHT_CLICK_AIR) {
			if(player.getItemInHand()==null) return;
			if(player.getItemInHand().equals(item))
				player.openInventory(inv);
		}
	}
}
