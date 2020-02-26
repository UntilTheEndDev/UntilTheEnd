package HamsterYDS.UntilTheEnd.guide;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class CraftGuide implements Listener{
	public static UntilTheEnd plugin;
	public static int tot=10;
	public static HashMap<String,ArrayList<Inventory> > helps=new HashMap<String,ArrayList<Inventory> >();
	public static HashMap<ItemStack,Inventory> crafts=new HashMap<ItemStack,Inventory>();
	public static HashMap<String,ArrayList<Inventory> > playerInvs=new HashMap<String,ArrayList<Inventory> >();
	public static ArrayList<String> cheating=new ArrayList<String>();
	
	public static Inventory inv=Bukkit.createInventory(null,45,"UntilTheEnd:合成帮助");
	
	public CraftGuide(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		for(Player player:Bukkit.getOnlinePlayers()) 
			playerInvs.put(player.getName(),new ArrayList<Inventory>());
		new CraftGuide();
	}
	public CraftGuide() {
		loadNew();
		loadTypes();
	}
	public static void loadTypes() {
		ItemStack frame=getItem("§8边框",Material.STAINED_GLASS_PANE,15);
		for(int i=0;i<9;i++) inv.setItem(i,frame);
		inv.setItem(9,frame);inv.setItem(17,frame);inv.setItem(18,frame);inv.setItem(26,frame);inv.setItem(27,frame);inv.setItem(35,frame);
		for(int i=36;i<45;i++) inv.setItem(i,frame);
		UntilTheEndApi.GuideApi.addCategory("§6基础",Material.LEASH,(short) 0);
		UntilTheEndApi.GuideApi.addCategory("§6衣物",Material.GOLD_HELMET,(short) 0);
		UntilTheEndApi.GuideApi.addCategory("§6生存",Material.IRON_PICKAXE,(short) 0);
		UntilTheEndApi.GuideApi.addCategory("§6战斗",Material.GOLD_SWORD,(short) 0);
		UntilTheEndApi.GuideApi.addCategory("§6魔法",Material.SPLASH_POTION,(short) 0);
		UntilTheEndApi.GuideApi.addCategory("§6科学",Material.REDSTONE_COMPARATOR,(short) 0);
	}
	public static ArrayList<String> openers=new ArrayList<String>();
	@EventHandler public void onOpen(InventoryOpenEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getName().equalsIgnoreCase("UntilTheEnd:合成帮助")) openers.add(event.getPlayer().getName());
	}
	@EventHandler public void onClose(InventoryCloseEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getName().equalsIgnoreCase("UntilTheEnd:合成帮助")) {
			ArrayList<Inventory> invs=playerInvs.get(event.getPlayer().getName());
			invs.add(inv);
			playerInvs.remove(event.getPlayer().getName());
			playerInvs.put(event.getPlayer().getName(),invs);
			openers.remove(event.getPlayer().getName());
		}
	}
	@EventHandler public void onDrag(InventoryDragEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getName().equalsIgnoreCase("UntilTheEnd:合成帮助")) event.setCancelled(true);
	}
	@EventHandler public void onJoin(PlayerJoinEvent event) {
		Player player=event.getPlayer();
		playerInvs.put(player.getName(),new ArrayList<Inventory>());
	}
	@EventHandler public void onQuit(PlayerQuitEvent event) {
		Player player=event.getPlayer();
		playerInvs.remove(player.getName());
	}
	@EventHandler public void onClick(InventoryClickEvent event) {
		Player player=(Player) event.getWhoClicked();
		Inventory inv=event.getClickedInventory();
		if(inv==null) return;
		if(!inv.getName().equalsIgnoreCase("UntilTheEnd:合成帮助")) return;
		//TODO
		event.setCancelled(true);
		
		ItemStack item=event.getCurrentItem().clone();
		if(item==null) return;
		if(item.getItemMeta()==null) return;
		item.setAmount(1);
		if(event.getSlot()==8) 
			player.openInventory(this.inv);
		if(event.getSlot()==0) {
			ArrayList<Inventory> invs=playerInvs.get(player.getName());
			if(invs.size()==0) 
				player.openInventory(this.inv);
			else {
				player.openInventory(invs.get(invs.size()-1));
				invs.remove(invs.size()-1);
				playerInvs.remove(player.getName());
				playerInvs.put(player.getName(),invs);
			}
		}
		if(inv.getSize()==27&&event.getSlot()==11) return;
//		if(event.getSlot()==36) {
//			int index=helps.get(key);
//		}
//		if(event.getSlot()==45) {
//			//下一页
//		}
		if(crafts.containsKey(item)) {
			if(cheating.contains(player.getName()))
				if(crafts.get(item).getSize()==27) {
					event.setCancelled(true);
					event.setCursor(item);
					return;
				}
			ArrayList<Inventory> invs=playerInvs.get(player.getName());
			invs.add(inv);
			playerInvs.remove(player.getName());
			playerInvs.put(player.getName(),invs);
			player.openInventory(crafts.get(item));
		}
	}
	public static void addItem(String string, ItemStack item) {
		ArrayList<Inventory> invs=helps.get(string);
		Inventory cinv=invs.get(invs.size()-1);
		int index=0;
		for(int slot=0;slot<cinv.getSize();slot++) 
			if(cinv.getItem(slot)==null) {
				index=slot;
				break;
			}
		cinv.setItem(index,item);
		invs.set(invs.size()-1,cinv);
		helps.remove(string);
		helps.put(string,invs);
	}
	//获取一个物品
	public static ItemStack getItem(String name,Material material,int data) {
		ItemStack item=new ItemStack(material);
		item.setDurability((short) data);
		ItemMeta meta=item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	//获取一个新的类型GUI（多物品）
	public static Inventory loadNew() {
		ItemStack frame=getItem("§8边框",Material.STAINED_GLASS_PANE,15);
		Inventory inv=Bukkit.createInventory(null,45,"UntilTheEnd:合成帮助");
		/**/
		for(int i=0;i<9;i++) inv.setItem(i,frame);
		inv.setItem(9,frame);inv.setItem(17,frame);
		inv.setItem(18,frame);inv.setItem(26,frame);
		inv.setItem(27,frame);inv.setItem(35,frame);
		for(int i=36;i<45;i++) inv.setItem(i,frame);
		/**/
		ItemStack last=getItem("§a上一页",Material.STAINED_GLASS_PANE,4);
		ItemStack next=getItem("§a下一页",Material.STAINED_GLASS_PANE,11);
		inv.setItem(36,last);inv.setItem(44,next);
		ItemStack back=getItem("§a返回上一层",Material.STAINED_GLASS_PANE,6);
		ItemStack menu=getItem("§a返回主菜单",Material.STAINED_GLASS_PANE,8);
		inv.setItem(0,back);inv.setItem(8,menu);
		return inv;
	}
	//获取一整个好多GUI
	public static ArrayList<Inventory> getTypeInventory() {
		ArrayList<Inventory> invs=new ArrayList<Inventory>();
		invs.add(loadNew());
		return invs;
	}
	//加载一个新的物品合成展示GUI
	public static Inventory getCraftInventory() {
		ItemStack frame=getItem("§8边框",Material.STAINED_GLASS_PANE,15);
		Inventory craftInv=Bukkit.createInventory(null,27,"UntilTheEnd:合成帮助");
		for(int i=0;i<9;i++) craftInv.setItem(i,frame);
		craftInv.setItem(9,frame);craftInv.setItem(17,frame);
		for(int i=18;i<27;i++) craftInv.setItem(i,frame);
		return craftInv;
	}
}
