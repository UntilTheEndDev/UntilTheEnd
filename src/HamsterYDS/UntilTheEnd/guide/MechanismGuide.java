package HamsterYDS.UntilTheEnd.guide;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
public class MechanismGuide implements Listener{
	public static UntilTheEnd plugin;
	public static Inventory inv=Bukkit.createInventory(null,45,"UntilTheEnd:机制帮助");
	public MechanismGuide(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		new MechanismGuide();
	}
	public MechanismGuide() {
		ItemStack frame=getItem1("§8边框",Material.STAINED_GLASS_PANE,15);
		for(int i=0;i<9;i++) inv.setItem(i,frame);
		inv.setItem(9,frame);inv.setItem(17,frame);inv.setItem(18,frame);inv.setItem(26,frame);inv.setItem(27,frame);inv.setItem(35,frame);
		for(int i=36;i<45;i++) inv.setItem(i,frame);
		
		List<String> hudLores=new ArrayList<String>();
		hudLores.add("§c§l玩家死亡将会回复原位");
		hudLores.add("§e§l正常属性：");
		hudLores.add("§d理智值§8-§d200");
		hudLores.add("§d温度§8-§d15-55°C");
		hudLores.add("§d湿度§8-§d小于10");
		ItemStack hud=getItem2("§6固有属性",Material.STAINED_GLASS,5,hudLores); inv.setItem(19,hud);
		
		List<String> sanLores=new ArrayList<String>();
		sanLores.add("§8玩家§d固有§8属性-表示玩家的§d清醒程度");
		sanLores.add("§d§l控制因素(包括但不限于)：");
		sanLores.add("§8▷身旁有§e§l怪物↓§8或§e§l玩家↑");
		sanLores.add("§8▷湿度§e§l过高↓");
		sanLores.add("§8▷穿§e§l特殊衣物↑↓");
		sanLores.add("§8▷身上或持有§e§l特殊物品↑↓");
		sanLores.add("§8▷§e§l傍晚↓夜晚↓↓");
		sanLores.add("§8▷吃§e§l特殊食物↑↓");
		sanLores.add("§8▷亮度§e§l过低↓");
		sanLores.add("§d§l影响：");
		sanLores.add("§8▷反胃§c<=§l120");
		sanLores.add("§8▷打字乱码§c<=§l60");
		sanLores.add("§8▷无法说话§c<=§l30");
		sanLores.add("§8▷生物变形(例:玩家变成僵尸)§c<=§l30");
		sanLores.add("§8▷梦魇出现§c<=§l20");
		ItemStack sanity=getItem2("§r§3§k§m§l-----------§r§6理智值§r§3§k§m§l-----------",Material.SKULL_ITEM,3,sanLores); inv.setItem(21,sanity);
		
		List<String> humLores=new ArrayList<String>();
		humLores.add("§8玩家§d固有§8属性-表示玩家的§d潮湿程度");
		humLores.add("§d§l控制因素(包括但不限于)：");
		humLores.add("§8▷在§e§l雨§8中↑");
		humLores.add("§8▷在§e§l水§8中↑");
		humLores.add("§d§l影响：");
		humLores.add("§8▷背包物品变§c潮湿");
		humLores.add("§8▷背包内一些物品会§c变形§8(例如石头变成苔石)");
		humLores.add("§8▷无法打出§c群体§8伤害");
		humLores.add("§8▷潮湿食物使用效果§c减半");
		humLores.add("§8▷潮湿物品无法§c用于合成");
		ItemStack humidity=getItem2("§r§3§k§m§l-----------§r§6湿度§r§3§k§m§l-----------",Material.WATER_BUCKET,0,humLores); inv.setItem(23,humidity);
		
		List<String> temLores=new ArrayList<String>();
		temLores.add("§8玩家§d固有§8属性-表示玩家的§d体表温度");
		temLores.add("§d§l控制因素(包括但不限于)：");
		temLores.add("§8▷§e§l季节的变化§8（冬冷夏热）");
		temLores.add("§8▷§e§l周围方块§8的布置");
		temLores.add("§8▷湿度§e§l过高↓");
		temLores.add("§8▷身着或持有§e§l特殊物品↑↓");
		temLores.add("§d§l玩家自身温度影响：");
		temLores.add("§8▷冰冻状态(缓慢效果+扣血)");
		temLores.add("§8▷炎热状态(扣血*2)");
		temLores.add("§d§l环境温度影响：");
		temLores.add("§8▷周围方块闷烧§c>=§l60°C");
		temLores.add("§8▷周围方块闷烧加速§c>=§l65°C");
		ItemStack temperature=getItem2("§r§3§k§m§l-----------§r§6温度§r§3§k§m§l-----------",Material.ICE,0,temLores); inv.setItem(25,temperature);
	}
	public static ArrayList<String> openers=new ArrayList<String>();
	@EventHandler public void onOpen(InventoryOpenEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getName().equalsIgnoreCase("UntilTheEnd:机制帮助")) openers.add(event.getPlayer().getName());
	}
	@EventHandler public void onClose(InventoryCloseEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getName().equalsIgnoreCase("UntilTheEnd:机制帮助")) openers.remove(event.getPlayer().getName());
	}
	@EventHandler public void onClick(InventoryClickEvent event) {
		Player player=(Player) event.getWhoClicked();
		if(openers.contains(player.getName())) event.setCancelled(true);
	}
	@EventHandler public void onDrag(InventoryDragEvent event) {
		Player player=(Player) event.getWhoClicked();
		if(openers.contains(player.getName())) event.setCancelled(true);
	}
	private static ItemStack getItem1(String name,Material material,int data) {
		ItemStack item=new ItemStack(material);
		item.setDurability((short) data);
		ItemMeta meta=item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	private static ItemStack getItem2(String name,Material material,int data,List<String> lores) {
		ItemStack item=new ItemStack(material);
		item.setDurability((short) data);
		ItemMeta meta=item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lores);
		item.setItemMeta(meta);
		return item;
	}
}
