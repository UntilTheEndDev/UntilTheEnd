package ute.item.magic;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ute.UntilTheEnd;
import ute.api.BlockApi;
import ute.api.event.block.CustomBlockBreakEvent;
import ute.api.event.block.CustomBlockInteractEvent;
import ute.api.event.block.CustomBlockPlaceEvent;
import ute.item.ItemManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Teleportage extends BukkitRunnable implements Listener{
	public static HashMap<String, TeleportPoint> teleportages = new HashMap<String, TeleportPoint>();
    public Teleportage() {
        loadBlocks();
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
        runTaskTimer(UntilTheEnd.getInstance(), 0L, UntilTheEnd.getInstance().getConfig().getLong("block.fresh") * 20);
    }
    @Override
    public void run() {saveBlocks();}
    public static class TeleportPoint {
		List<String> permissioners;
		String master; // UUID
		String name;
	}

	@EventHandler
	public void addBlock(CustomBlockPlaceEvent event) {
		if (!event.getItem().id.equalsIgnoreCase("Teleportage"))
			return;
		Player player = event.getPlayer();
		Block block = event.getBlock();
		String loc = BlockApi.locToStr(block.getLocation());
		TeleportPoint point = new TeleportPoint();
		point.name = "§6§l" + player.getName() + "的传送点";
		point.permissioners = new ArrayList<String>();
		point.master = player.getUniqueId().toString();
		teleportages.put(loc, point);
		saveBlocks();
	}

	@EventHandler
    public void onInteract(CustomBlockInteractEvent event){
	    if(event.getCustomItem().id.equalsIgnoreCase("Teleportage")&&(!event.isCancelled())&&event.getWho().isSneaking()){
	        TeleportPoint point=teleportages.get(BlockApi.locToStr(event.getClickedBlock().getLocation()));
	        if(point==null) return;
	        Player player=event.getWho();
	        if(player.getUniqueId().toString().equalsIgnoreCase(point.master)){
                event.setCancelled(true);
	            player.sendMessage("§c[UntilTheEnd]§r 输入您给予或删除传送权限的玩家对象的名字");
                player.sendMessage("§c[UntilTheEnd]§r 给予请在名字前加“+”，删除则使用“-”");
                waitRequires.put(player.getUniqueId(),BlockApi.locToStr(event.getClickedBlock().getLocation()));
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        if(waitRequires.containsKey(player.getUniqueId())){
                            player.sendMessage("§c[UntilTheEnd]§r 操作因为时间过长而取消");
                            waitRequires.remove(player.getUniqueId());
                        }
                    }
                }.runTaskLater(UntilTheEnd.getInstance(),600L);
            }
        }
    }
    private static HashMap<UUID,String> waitRequires=new HashMap<UUID,String>();
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
	    Player player=event.getPlayer();
	    String message=event.getMessage();
	    if(waitRequires.containsKey(player.getUniqueId())){

	        event.setCancelled(true);
	        String loc=waitRequires.get(player.getUniqueId());
            TeleportPoint point=teleportages.get(loc);

            if(point==null) return;

            if(message.startsWith("+"))
                point.permissioners.add(message.replace("+",""));
            if(message.startsWith("-"))
                point.permissioners.remove(message.replace("-",""));

            waitRequires.remove(player.getUniqueId());
            player.sendMessage("§c[UntilTheEnd]§r 操作成功");
            teleportages.remove(loc);
            teleportages.put(loc,point);
        }
    }

	@EventHandler
	public static void removeBlock(CustomBlockBreakEvent event) {
		if (!event.getCustomItem().id.equalsIgnoreCase("Teleportage"))
			return;
		Player player = event.getPlayer();
		Block block = event.getBlock();
		String loc = BlockApi.locToStr(block.getLocation());
		teleportages.remove(loc);
		saveBlocks();
	}

	public static void saveBlocks() {
		File file = new File(ItemManager.plugin.getDataFolder() + "/data/", "teleportages.yml");
		file.delete();
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		for (String loc : teleportages.keySet()) {
			TeleportPoint teleportage = teleportages.get(loc);
			yaml.set(loc + ".master", teleportage.master);
			yaml.set(loc + ".name", teleportage.name);
			yaml.set(loc + ".permissioners", teleportage.permissioners);
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
		teleportages.clear();
		for (String loc : yaml.getKeys(false)) {
			TeleportPoint point = new TeleportPoint();
			point.master = yaml.getString(loc + ".master");
			point.name = yaml.getString(loc + ".name");
			point.permissioners = yaml.getStringList(loc + ".permissioners");
			teleportages.put(loc, point);
		}
	}
}
