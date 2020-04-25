package HamsterYDS.UntilTheEnd.item.magic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import HamsterYDS.UntilTheEnd.api.BlockApi;
import HamsterYDS.UntilTheEnd.event.block.CustomBlockBreakEvent;
import HamsterYDS.UntilTheEnd.event.block.CustomBlockPlaceEvent;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class Teleportage implements Listener {
	public static HashMap<String, TeleportPoint> teleportages = new HashMap<String, TeleportPoint>();

	public static class TeleportPoint {
		List<String> permissioners; // UUID
		String master; // UUID
		String name;
	}

	public Teleportage() {
		loadBlocks();
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
	}

	@EventHandler
	public static void addBlock(CustomBlockPlaceEvent event) {
		if (!event.getItem().id.equalsIgnoreCase("Teleportage"))
			return;
		Player player = event.getPlayer();
		Block block = event.getBlock();
		String loc = BlockApi.locToStr(block.getLocation());
		TeleportPoint point = new TeleportPoint();
		point.name = player.getName() + "的传送点";
		point.permissioners = new ArrayList<String>();
		point.master = player.getUniqueId().toString();
		teleportages.put(loc, point);
		saveBlocks();
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
