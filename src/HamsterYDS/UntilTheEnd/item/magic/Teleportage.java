package HamsterYDS.UntilTheEnd.item.magic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.api.BlockApi;
import HamsterYDS.UntilTheEnd.event.block.CustomBlockBreakEvent;
import HamsterYDS.UntilTheEnd.event.block.CustomBlockInteractEvent;
import HamsterYDS.UntilTheEnd.event.block.CustomBlockPlaceEvent;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class Teleportage implements Listener {
    public static HashMap<TeleportMaster,TeleportPoint> teleportages=new HashMap<TeleportMaster,TeleportPoint>();
    public static class TeleportMaster{
    	String master; //UUID
    	String loc;
    }
    public static class TeleportPoint{
    	List<String> permissioners; //UUID
    	String name;
    }

    public Teleportage() {
    	loadBlocks();
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

	private static HashMap<String, Integer> cd = new HashMap<String, Integer>();
    private static HashMap<String,String> selectors=new HashMap<String,String>();
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRight(CustomBlockInteractEvent event) {
    	if(!event.hasBlock()) return;
    	Player player=event.getPlayer();
    	if(!player.isSneaking()) return;
    	Block block=event.getClickedBlock();
    	String toString=BlockApi.locToStr(block.getLocation());
    	if(event.getCustomItem().id.equalsIgnoreCase("Teleportage")) {
    		TeleportMaster master=new TeleportMaster();
    		master.loc=toString;master.master=player.getUniqueId().toString();
    		if(!teleportages.containsKey(master)) return;
    		selectors.put(player.getName(),toString);
    		player.sendMessage("§6[§cUntilTheEnd§6]§r 在聊天栏输入你想开放权限的玩家名字");
    		new BukkitRunnable() {

				@Override
				public void run() {
					if(selectors.containsKey(player.getName())) {
						selectors.remove(player.getName());
						player.sendMessage("§6[§cUntilTheEnd§6]§r 自动取消设置");
					}
				}
    			
    		}.runTaskLater(ItemManager.plugin,500L);
    	}
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
    	Player player=event.getPlayer();
    	if(selectors.containsKey(player.getName())) {
    		String message=event.getMessage();
    		OfflinePlayer setter=Bukkit.getPlayer(message);
    		if(setter==null) setter=Bukkit.getOfflinePlayer(message);
    		if(setter==null) {
    			selectors.remove(player.getName());
				player.sendMessage("§6[§cUntilTheEnd§6]§r 玩家不存在,自动取消设置");
    			return;
    		}
    		TeleportMaster master=new TeleportMaster();
    		master.loc=selectors.get(player.getName());master.master=player.getUniqueId().toString();
    		
    		teleportages.get(master).permissioners.add(setter.getUniqueId().toString());
    		selectors.remove(player.getName());
    		
    		player.sendMessage("§6[§cUntilTheEnd§6]§r 设置成功");
    	}
    }
    
    @EventHandler 
    public static void addBlock(CustomBlockPlaceEvent event) {
    	Player player=event.getPlayer();
    	Block block=event.getBlock();
    	String loc=BlockApi.locToStr(block.getLocation());
    	TeleportMaster master=new TeleportMaster();
    	master.loc=loc;master.master=player.getUniqueId().toString();
    	TeleportPoint point=new TeleportPoint();
    	point.name=player.getName()+"的传送点";
    	point.permissioners=new ArrayList<String>();
    	teleportages.put(master, point);
    }
    
    @EventHandler 
    public static void removeBlock(CustomBlockBreakEvent event) {
    	Player player=event.getPlayer();
    	Block block=event.getBlock();
    	String loc=BlockApi.locToStr(block.getLocation());
    	TeleportMaster master=new TeleportMaster();
    	master.loc=loc;master.master=player.getUniqueId().toString();
    	teleportages.remove(master);
    }
    
    public static void saveBlocks() {
        File file = new File(ItemManager.plugin.getDataFolder() + "/data/", "teleportages.yml");
        file.delete();
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for (TeleportMaster teleportage : teleportages.keySet()){
        	yaml.set(teleportage.loc+".master",teleportage.master.toString());
        	yaml.set(teleportage.loc+".name",teleportages.get(teleportage).name);
        	List<String> toStrings=new ArrayList<String>();
        	List<String> permissioners=teleportages.get(teleportage).permissioners;
        	yaml.set(teleportage.loc+".permissioners",toStrings);
        }
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBlocks() {
        File file = new File(ItemManager.plugin.getDataFolder() + "/data/", "teleportages.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        teleportages=new HashMap<TeleportMaster,TeleportPoint>();
        for (String loc : yaml.getKeys(true)){
        	TeleportMaster master=new TeleportMaster();
        	master.loc=loc;master.master=yaml.getString(loc+".master");
        	TeleportPoint point=new TeleportPoint();
        	point.name=yaml.getString(loc+".name");
        	point.permissioners=yaml.getStringList(loc+".permissioners");
        	teleportages.put(master, point);
        }
    }
}
