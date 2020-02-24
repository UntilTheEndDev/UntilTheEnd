package HamsterYDS.UntilTheEnd;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.cap.tem.NaturalTemperature;
import HamsterYDS.UntilTheEnd.guide.Guide;
import HamsterYDS.UntilTheEnd.item.ItemProvider;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.world.WorldState;
import HamsterYDS.UntilTheEnd.world.WorldState.IWorld;
import HamsterYDS.UntilTheEnd.world.WorldState.Season;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Commands implements CommandExecutor, Listener{
	
	public static UntilTheEnd plugin;
	public static ArrayList<String> cmdTab=new ArrayList<String>();
	public static ArrayList<String> seasonTab=new ArrayList<String>();
	public static ArrayList<String> itemTab=new ArrayList<String>();
	public static ArrayList<String> worldTab=new ArrayList<String>();
	public static ArrayList<String> capTab=new ArrayList<String>();
	public Commands(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getCommand("ute").setExecutor(this);
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		for(String s:ItemProvider.items.keySet()) 
			itemTab.add(s);
		cmdTab.add("give");
		cmdTab.add("guide");
		cmdTab.add("material");
		cmdTab.add("entitytype");
		cmdTab.add("set");
		cmdTab.add("season");
		for(Season season:Season.values()) 
			seasonTab.add(season.toString());
		Collections.sort(itemTab);
		Collections.sort(cmdTab);
		for(World world:Bukkit.getWorlds()) {
			if(Config.disableWorlds.contains(world.getName())) continue;
			worldTab.add(world.getName());
		}
		capTab.add("san");
		capTab.add("hum");
		capTab.add("tem");
	}
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String lb, String[] ct) {
		if(!lb.equalsIgnoreCase("ute")) return true;
		if(ct.length==0) sendAll(cs);
		if(ct.length>=1) { //大于等于1
			if(ct[0].equalsIgnoreCase("help")) //ute help
				sendAll(cs);
			if(ct[0].equalsIgnoreCase("guide")) { //ute guide
				if(!(cs instanceof Player)) //is Console
					notPlayer(cs);
				else {
					Player player=(Player) cs;
					if(!cs.hasPermission("ute.guide"))
						notPermitted(cs);
					else player.getInventory().addItem(Guide.item);
				}
			}
			if(ct[0].equalsIgnoreCase("material")) { //ute material
				if(!(cs instanceof Player)) //is Console
					notPlayer(cs);
				else {
					Player player=(Player) cs;
					if(!cs.hasPermission("ute.material"))
						notPermitted(cs);
					else player.sendMessage(player.getLocation().getBlock().getType().toString());
				}
			}
			if(ct[0].equalsIgnoreCase("entitytype")) { //ute entitytype
				if(!(cs instanceof Player)) //is Console
					notPlayer(cs);
				else {
					Player player=(Player) cs;
					if(!cs.hasPermission("ute.entitytype"))
						notPermitted(cs);
					else 
						for(Entity entity:player.getNearbyEntities(1.0,1.0,1.0)) 
							player.sendMessage(entity.getType().toString());
				}
			}
		}
		if(ct.length>=3) {
			if(ct[0].equalsIgnoreCase("season")) { //ute season
				if(!cs.hasPermission("ute.season"))
					notPermitted(cs);
				else {
					try {Season.valueOf(ct[1]);} 
					catch(Exception e) {
						cs.sendMessage(Config.getLang("cmd.notASeason"));
						return true;
					}Season season=Season.valueOf(ct[1]);
					try {Integer.valueOf(ct[2]);} 
					catch(Exception e) {
						cs.sendMessage(Config.getLang("cmd.notANumber"));
						return true;
					}int day=Integer.valueOf(ct[2]);
					if(ct.length==3) {
						Player player=(Player) cs;
						changeSeason(player.getWorld(),season,day);
					}
					if(ct.length==4) {
						World setWorld=Bukkit.getWorld(ct[3]);
						if(setWorld==null) {
							cs.sendMessage(Config.getLang("cmd.notAWorld"));
							return true;
						}
						changeSeason(setWorld,season,day);
						WorldState.DayCounter.tellPlayers(setWorld);
					}
					cs.sendMessage(Config.getLang("cmd.setSeason")); 
				}
			}
			if(ct[0].equalsIgnoreCase("give")) { //ute give
				if(!cs.hasPermission("ute.give")) 
					notPermitted(cs);
				else {
					String playerName=ct[1];
					String itemName=ct[2];
					int amount=1;
					if(ct.length==4)
						try {Integer.valueOf(ct[3]);} 
						catch(Exception e) {
							cs.sendMessage(Config.getLang("cmd.notANumber"));
							return true;
						}amount=Integer.valueOf(ct[3]);
					if(amount<=0) {
						cs.sendMessage(Config.getLang("cmd.notPositive"));
						return true;
					}
					Player givee=Bukkit.getPlayer(playerName);
					ItemStack item=ItemProvider.items.get(itemName);
					if(item==null) {
						cs.sendMessage(Config.getLang("cmd.notAnItem"));
						return true;
					}
					if(givee==null) {
						cs.sendMessage(Config.getLang("cmd.notAPlayer"));
						return true;
					}
					item=item.clone();
					item.setAmount(amount); 
					givee.getInventory().addItem(item);
					String message=Config.getLang("cmd.giveItem");
					message=message.replace("%item%",item.getItemMeta().getDisplayName());
					message=message.replace("%player%",playerName);
					cs.sendMessage(message);
				}
			}
		}
		if(ct.length>=4) {
			if(ct[0].equalsIgnoreCase("set")) { //ute set
				if(!cs.hasPermission("ute.set"))
					notPermitted(cs);
				else {
					String playerName=ct[1];
					Player player=Bukkit.getPlayer(playerName);
					if(player==null) {
						cs.sendMessage(Config.getLang("cmd.notAPlayer"));
						return true;
					}
					if(!player.isOnline()) {
						cs.sendMessage(Config.getLang("cmd.notAPlayer"));
						return true;
					}
					String typeName=ct[2];
					try {Integer.valueOf(ct[3]);} 
					catch(Exception e) {
						cs.sendMessage(Config.getLang("cmd.notANumber"));
						return true;
					}
					int value=Integer.valueOf(ct[3]);
					PlayerManager.set(playerName,typeName,value);
					PlayerManager.save(playerName);
					cs.sendMessage(Config.getLang("cmd.setHud"));
				}
			}
		}
		return true;
	}
	public static void sendAll(CommandSender CommandSender){
		CommandSender.sendMessage(Config.getLang("cmd.label_1"));
		send合成(CommandSender);
		send季节(CommandSender);
		send给予(CommandSender);
		send设置(CommandSender);
		CommandSender.sendMessage(Config.getLang("cmd.label_2"));
		sendMaterial(CommandSender);
		sendEntityType(CommandSender);
	}
	public static void send合成(CommandSender CommandSender) {
		CommandSender.sendMessage(Config.getLang("cmd.ute_guide"));
	}
	public static void send季节(CommandSender CommandSender) {
		CommandSender.sendMessage(Config.getLang("cmd.ute_season"));
	}
	public static void send给予(CommandSender CommandSender) {
		CommandSender.sendMessage(Config.getLang("cmd.ute_give"));
	}
	public static void send设置(CommandSender CommandSender) {
		CommandSender.sendMessage(Config.getLang("cmd.ute_set"));
	}
	public static void sendMaterial(CommandSender CommandSender) {
		CommandSender.sendMessage(Config.getLang("cmd.ute_material"));
	}
	public static void sendEntityType(CommandSender CommandSender) {
		CommandSender.sendMessage(Config.getLang("cmd.ute_entitytype"));
	}
	public static void notPlayer(CommandSender CommandSender) {
		CommandSender.sendMessage(Config.getLang("cmd.notPlayer"));
	}
	public static void notPermitted(CommandSender CommandSender) {
		CommandSender.sendMessage(Config.getLang("cmd.noPermission"));
	}
	public static void changeSeason(World setWorld,Season season,int day) {
		IWorld world=new WorldState().new IWorld(season, day);
		WorldState.worldStates.remove(setWorld.getName());
		WorldState.worldStates.put(setWorld.getName(),world);
		NaturalTemperature.addTem(setWorld);
	}
	@EventHandler public void onTab(TabCompleteEvent event) {
		String label=event.getBuffer();
		String first="";
		label=label.replace("/","");
		if(label.contains("ute give")) {
			int tot=0;
			for(int i=0;i<label.length();i++) {
				if(tot==3) 
					first+=label.charAt(i);
				if(label.charAt(i)==' ')
					tot++;
			}
			if(tot==3) {
				ArrayList<String> newTab=new ArrayList<String>();
				for(String s:itemTab) 
					if((s.toLowerCase()).startsWith(first.toLowerCase())) 
						newTab.add(s);
				event.setCompletions(newTab);
			}
			return;
		}
		if(label.contains("ute set")) {
			int tot=0;
			for(int i=0;i<label.length();i++) {
				if(tot==3) 
					first+=label.charAt(i);
				if(label.charAt(i)==' ')
					tot++;
			}
			if(tot==3) {
				ArrayList<String> newTab=new ArrayList<String>();
				for(String s:capTab) 
					if((s.toLowerCase()).startsWith(first.toLowerCase())) 
						newTab.add(s);
				event.setCompletions(newTab);
			}
			return;
		}
		if(label.contains("ute season ")) {
			first=label.replace("ute season ","");
			if(first.contains(" ")) {
				if(first.equals(" ")) return;
				first="";
				int tot=0;
				for(int i=0;i<label.length();i++) {
					if(tot==4) 
						first+=label.charAt(i);
					if(label.charAt(i)==' ')
						tot++;
				}
				if(tot==4) {
					ArrayList<String> newTab=new ArrayList<String>();
					for(String s:worldTab) 
						if((s.toLowerCase()).startsWith(first.toLowerCase())) 
							newTab.add(s);
					event.setCompletions(newTab);
				}
				return;
			}else {
				ArrayList<String> newTab=new ArrayList<String>();
				for(String s:seasonTab) 
					if((s.toLowerCase()).startsWith(first.toLowerCase())) 
						newTab.add(s);
				event.setCompletions(newTab);
				return;
			}
		}
		if(label.contains("ute ")) {
			first=label.replace("ute ","");
			if(first.contains(" ")) return;
			ArrayList<String> newTab=new ArrayList<String>();
			for(String s:cmdTab) 
				if((s.toLowerCase()).startsWith(first.toLowerCase())) 
					newTab.add(s);
			event.setCompletions(newTab);
			return;
		}
	}
//	public static int check(String cmd) {
//		
//	}
}