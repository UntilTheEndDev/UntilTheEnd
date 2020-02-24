package HamsterYDS.UntilTheEnd.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;

import HamsterYDS.UntilTheEnd.block.BlockManager;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.item.ItemProvider;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.world.WorldState;
import HamsterYDS.UntilTheEnd.world.WorldState.Season;

public class UntilTheEndApi {
	public static class BlockApi{
		public static String getSpecialBlock(Location loc) {
			World world=loc.getWorld();
			String toString=world.getName()+"-"+loc.getBlockX()+"-"+loc.getBlockY()+"-"+loc.getBlockZ();
			return BlockManager.blocks.get(toString);
		}
		public static HashMap<String,String> getSpecialBlocks(World world){
			HashMap<String,String> map=new HashMap<String,String>();
			for(String str:BlockManager.blocks.keySet()) {
				if(!str.startsWith(world.getName())) continue;
				String name=BlockManager.blocks.get(str);
				map.put(str,name);
			}
			return map;
		}
		public static ArrayList<String> getSpecialBlocks(String name){
			ArrayList<String> list=new ArrayList<String>();
			if(!BlockManager.blockDatas.containsKey(name)) return list;
			for(String str:BlockManager.blockDatas.get(name)) {
				list.add(str);
			}
			return list;
		}
		public static HashMap<String,String> getSpecialBlocks(){
			HashMap<String,String> map=new HashMap<String,String>();
			for(String str:BlockManager.blocks.keySet()) {
				String name=BlockManager.blocks.get(str);
				map.put(str,name);
			}
			return map;
		}
		public static Location strToLoc(String toString) {
			String world="",x="",y="",z="";
			int tot=0;
			for(int i=0;i<toString.toCharArray().length;i++) {
				char ch=toString.toCharArray()[i];
				if(ch=='-') {
					tot++;
					if(toString.toCharArray()[i+1]=='-') 
						tot--;
					continue;
				}
				if(tot==0) world+=ch;
				if(tot==1) x+=ch;
				if(tot==2) y+=ch;
				if(tot==3) z+=ch;
			}
			return new Location(Bukkit.getWorld(world),Integer.valueOf(x),Integer.valueOf(y),Integer.valueOf(z));
		}
		public static String locToStr(Location loc) {
			World world=loc.getWorld();
			String toString=world.getName();
			toString=toString+loc.getBlockX()+"-"+loc.getBlockY()+"-"+loc.getBlockZ();
			return toString;
		}
	}
	public static class HudApi{
		public static List<BossBar> getBars(Player player){
			return BossBarAPI.getBossBars(player);
		}
	}
	public static class ItemApi{
		public static HashMap<String,ItemStack> getItems(){
			return ItemProvider.items;
		}
		public static ItemStack getItem(String key) {
			return ItemProvider.getItem(key);
		}
	}
	public static class PlayerApi{
		public static int getValue(Player player,String type) {
			return PlayerManager.check(player.getName(),type);
		}
		public static void setValue(Player player,String type,int value) {
			PlayerManager.change(player.getName(),type,value);
		}
	}
	public static class WorldApi{
		public static Season getSeason(World world) {
			return WorldState.worldStates.get(world.getName()).season;
		}
		public static int getDay(World world) {
			return WorldState.worldStates.get(world.getName()).day;
		}
	}
	public static class GuideApi{
		public static void addItemToCategory(String category,ItemStack item) {
			CraftGuide.addItem(category, item);
		}
		public static void addItemCraftInv(String itemName,Inventory guideInventory) {
			CraftGuide.helps.put(itemName,guideInventory);
		}
	}
}
