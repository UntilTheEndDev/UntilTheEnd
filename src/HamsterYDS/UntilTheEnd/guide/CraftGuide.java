package HamsterYDS.UntilTheEnd.guide;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class CraftGuide implements Listener{
	public static UntilTheEnd plugin;
	public static HashMap<String,Inventory> helps=new HashMap<String,Inventory>();
	public static Inventory inv=Bukkit.createInventory(null,45,"UntilTheEnd:合成帮助");
	public CraftGuide(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		new CraftGuide();
	}
	public CraftGuide() {
		ItemStack frame=getItem("§8边框",Material.STAINED_GLASS_PANE,15);
		for(int i=0;i<9;i++) inv.setItem(i,frame);
		inv.setItem(9,frame);inv.setItem(17,frame);inv.setItem(18,frame);inv.setItem(26,frame);inv.setItem(27,frame);inv.setItem(35,frame);
		for(int i=36;i<45;i++) inv.setItem(i,frame);
		ItemStack 基础=getItem("§6基础",Material.LEASH,0);
		ItemStack 生存=getItem("§6生存",Material.IRON_PICKAXE,0);
		ItemStack 衣物=getItem("§6衣物",Material.GOLD_HELMET,0);
		ItemStack 战斗=getItem("§6战斗",Material.GOLD_SWORD,0);
		ItemStack 魔法=getItem("§6魔法",Material.SPLASH_POTION,0);
		ItemStack 科学=getItem("§6科学",Material.REDSTONE_COMPARATOR,0);
		helps.put("§6基础",getPartInventory());
		helps.put("§6生存",getPartInventory());
		helps.put("§6衣物",getPartInventory());
		helps.put("§6战斗",getPartInventory());
		helps.put("§6魔法",getPartInventory());
		helps.put("§6科学",getPartInventory());
		inv.setItem(10,基础);
		inv.setItem(11,生存);
		inv.setItem(12,衣物);
		inv.setItem(13,战斗);
		inv.setItem(14,魔法);
		inv.setItem(15,科学);
	}
	public static Inventory getPartInventory() {
		ItemStack frame=getItem("§8边框",Material.STAINED_GLASS_PANE,15);
		Inventory inv=Bukkit.createInventory(null,45,"UntilTheEnd:合成帮助");
		for(int i=0;i<9;i++) inv.setItem(i,frame);
		inv.setItem(9,frame);inv.setItem(17,frame);inv.setItem(18,frame);inv.setItem(26,frame);inv.setItem(27,frame);inv.setItem(35,frame);
		for(int i=36;i<45;i++) inv.setItem(i,frame);
		return inv;
	}
	public static Inventory getCraftInventory() {
		ItemStack frame=getItem("§8边框",Material.STAINED_GLASS_PANE,15);
		Inventory craftInv=Bukkit.createInventory(null,27,"UntilTheEnd:合成帮助");
		for(int i=0;i<9;i++) craftInv.setItem(i,frame);
		craftInv.setItem(9,frame);craftInv.setItem(17,frame);
		for(int i=18;i<27;i++) craftInv.setItem(i,frame);
		return craftInv;
	}
	private static ItemStack getItem(String name,Material material,int data) {
		ItemStack item=new ItemStack(material);
		item.setDurability((short) data);
		ItemMeta meta=item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	public static ArrayList<String> openers=new ArrayList<String>();
	@EventHandler public void onOpen(InventoryOpenEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getName().equalsIgnoreCase("UntilTheEnd:合成帮助")) openers.add(event.getPlayer().getName());
	}
	@EventHandler public void onClose(InventoryCloseEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getName().equalsIgnoreCase("UntilTheEnd:合成帮助")) openers.remove(event.getPlayer().getName());
	}
	@EventHandler public void onClick(InventoryClickEvent event) {
		Player player=(Player) event.getWhoClicked();
		Inventory inv=event.getClickedInventory();
		if(inv==null) return;
		if(openers.contains(player.getName())) 
			if(event.getAction()==InventoryAction.MOVE_TO_OTHER_INVENTORY)
				event.setCancelled(true); 
		if(!inv.getName().equalsIgnoreCase("UntilTheEnd:合成帮助")) return;
		ItemStack item=event.getClickedInventory().getItem(event.getSlot());
		event.setCancelled(true);
		if(item==null) return;
		if(item.getItemMeta()==null) return;
		if(inv.getSize()==27) {
			if(player.hasPermission("ute.guide.cheat")) {
				event.setCursor(event.getCurrentItem());
				event.setCancelled(true);
			}
		}else {
			if(helps.containsKey(item.getItemMeta().getDisplayName())) {
				player.openInventory(helps.get(item.getItemMeta().getDisplayName()));
			}
		}
	}
	@EventHandler public void onDrag(InventoryDragEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getName().equalsIgnoreCase("UntilTheEnd:合成帮助")) event.setCancelled(true);
	}
	public static void addItem(String string, ItemStack item) {
		Inventory cinv=helps.get(string);
		int index=0;
		for(int slot=0;slot<cinv.getSize();slot++) 
			if(cinv.getItem(slot)==null) {
				index=slot;
				break;
			}
		cinv.setItem(index,item);
		helps.remove(string);
		helps.put(string,cinv);
	}
}
