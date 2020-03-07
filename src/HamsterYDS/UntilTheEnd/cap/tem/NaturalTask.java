package HamsterYDS.UntilTheEnd.cap.tem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.cap.clothes.GetTem;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class NaturalTask {
	public static UntilTheEnd plugin = UntilTheEnd.getInstance();
	public NaturalTask(UntilTheEnd plugin) {
		new PlayerTask();
	}
	private static ArrayList<String> players=new ArrayList<String>();
	public static class PlayerTask extends BukkitRunnable{
		@Override
		public void run() {
			for(World world:Bukkit.getWorlds()) {
				if(!Config.enableWorlds.contains(world)) continue;
				for(Player player:world.getPlayers()) {
					int slot=hasWarmStone(player);
					if(slot!=-1) {
						toWarmStone(player,slot);
						continue;
					}
					if(players.contains(player.getName())) continue;
					int nt=BlockTemperature.getTemperature(player.getLocation());
					int pt=PlayerManager.check(player.getName(),"tem");
					int factor=GetTem.getTem(player);
					if(pt>nt) {
						players.add(player.getName());
						new BukkitRunnable() {
							@Override
							public void run() {
								players.remove(player.getName());
								PlayerManager.change(player.getName(),"tem",-1);
								cancel();
							}
						}.runTaskTimer(plugin,(factor-30)/15,1);
					}
					if(pt<nt) {
						players.add(player.getName());
						new BukkitRunnable() {
							@Override
							public void run() {
								players.remove(player.getName());
								PlayerManager.change(player.getName(),"tem",1);
								cancel();
							}
						}.runTaskTimer(plugin,(factor-30)/15,1);
					}
				}
			}
			for(Player player:Bukkit.getOnlinePlayers()) {
				if(!Config.enableWorlds.contains(player.getWorld())) continue;
				int pt=PlayerManager.check(player.getName(),"tem");
				if(pt<=5) 
					player.damage(0.5);
				if(pt<=0) 
					player.damage(0.5);
				if(pt>=60) 
					player.damage(0.5);
				if(pt>=65) 
					player.damage(0.5);
			}
		}
		private void toWarmStone(Player player,int slot) {
			PlayerInventory inv=player.getInventory();
			ItemStack item=inv.getItem(slot);
			List<String> lores=item.getItemMeta().getLore();
			int index=0;
			for(String str:lores) {
				if(str.contains("§8- §8§l温度 ")) {
					str=str.replace("§8- §8§l温度 ","");
					int tem=Integer.parseInt(str);
					int nt=BlockTemperature.getTemperature(player.getLocation());
					if(tem>nt){
						String newStr="§8- §8§l温度 "+(tem-1);
						lores.set(index,newStr);
					}
					if(tem<nt){
						String newStr="§8- §8§l温度 "+(tem+1);
						lores.set(index,newStr);
					}
					int pt=PlayerManager.check(player.getName(),"tem");
					if(pt>tem) 
						PlayerManager.change(player.getName(),"tem",-1);
					if(pt<tem) 
						PlayerManager.change(player.getName(),"tem",1);
					if(Math.random()>=0.85) {
						ItemMeta meta=item.getItemMeta();
						meta.setLore(lores);
						item.setItemMeta(meta);
					}
					return;
				}
				index++;
			}
		}
		private int hasWarmStone(Player player) {
			PlayerInventory inv=player.getInventory();
			for(int slot=0;slot<=60;slot++) {
				ItemStack item=inv.getItem(slot);
				if(item==null) continue;
				ItemMeta meta=item.getItemMeta();
				if(meta==null) continue;
				if(meta.getDisplayName()==null) continue;
				if(meta.getDisplayName().equalsIgnoreCase("§6暖石")) {
					return slot;
				}
			}
			return -1;
		}
		public PlayerTask() {
			runTaskTimer(plugin,0L,20L);
		}
	}
}
