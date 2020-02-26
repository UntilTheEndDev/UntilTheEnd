package HamsterYDS.UntilTheEnd.food;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerInventoryAdapt;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class RottenFood1 extends BukkitRunnable{
	public static UntilTheEnd plugin;
	public static ItemStack rottenFood=new ItemStack(Material.ROTTEN_FLESH);
	public RottenFood1(UntilTheEnd plugin) {
		this.plugin=plugin;
		
		ItemMeta meta=rottenFood.getItemMeta();
		meta.setDisplayName("§8§l腐烂食物");
		rottenFood.setItemMeta(meta);
		
		runTaskTimer(plugin,0L,plugin.getConfig().getInt("food.rotten.speed")*20);
	}
	public static void goBad(Player player) {
		PlayerInventory inv=player.getInventory();
		for(int slot=0;slot<60;slot++) {
			if(slot>=9&&slot<9+4) {
				int bag=PlayerInventoryAdapt.getBag(inv.getChestplate());
				if(bag==4) {
					if(Math.random()<=0.5)
						continue;
				}
			}
			ItemStack item=inv.getItem(slot);
			if(item==null) continue;
			if(item.getType().isEdible()) {
				inv.setItem(slot,setRotten(item,1));
			}
		}
	}
	public static ItemStack setRotten(ItemStack item,int v) {
		if(item==null) return new ItemStack(Material.AIR);
		if(getRot(item)<=0) {
			item=rottenFood;
			return item;
		}
		ItemMeta meta=item.getItemMeta();
		if(meta!=null) 
			if(meta.getDisplayName()!=null) 
				if(meta.getDisplayName().equalsIgnoreCase("§8§l腐烂食物")) 
					return item;
		List<String> lores=meta.getLore();
		int index=0;
		boolean flag=false;
		if(!meta.hasLore()) {
			lores=new ArrayList<String>();
		}
		for(String s:lores) {
			if(s.contains("§8- §8§l新鲜度 ")) {
				s=s.replace("§8- §8§l新鲜度 ","");
				int rot=Integer.valueOf(s);
				s=String.valueOf((rot-v)>=100?100:(rot-v));
				if(rot<=0) {
					item=rottenFood;
					return item;
				}
				lores.set(index,"§8- §8§l新鲜度 "+s);
				flag=true;
			}
			index++;
		}
		if(!flag) {
			lores.add("§8- §8§l新鲜度 100");
		}
		meta.setLore(lores);
		item.setItemMeta(meta);
		return item;
	}
	public static int getRot(ItemStack item) {
		ItemMeta meta=item.getItemMeta();
		List<String> lores=meta.getLore();
		if(!meta.hasLore()) lores=new ArrayList<String>();
		for(String s:lores) 
			if(s.contains("§8- §8§l新鲜度 ")) {
				s=s.replace("§8- §8§l新鲜度 ","");
				int rot=Integer.valueOf(s);
				return rot;
			}
		return 100;
	}
	@Override
	public void run() {
		for(World world:Config.enableWorlds)
			for(Player player:Bukkit.getOnlinePlayers()) 
				goBad(player);
	}
}
