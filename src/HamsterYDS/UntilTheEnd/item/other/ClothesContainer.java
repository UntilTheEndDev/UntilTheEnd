package HamsterYDS.UntilTheEnd.item.other;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.internal.EventHelper;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class ClothesContainer implements Listener {
	public static HashMap<UUID, Inventory> invs = new HashMap<UUID, Inventory>();
	public static ArrayList<UUID> openers = new ArrayList<UUID>();

	public ClothesContainer() {
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
	}

	@EventHandler
	public void onRight(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!event.hasItem())
			return;
		if (EventHelper.isRight(event.getAction())) {
			ItemStack item = event.getItem();
			if (ItemManager.isSimilar(item, getClass())) {
				Inventory inv = invs.get(player.getUniqueId());
				player.openInventory(inv);
				openers.add(player.getUniqueId());
			}
		}
	}

	@EventHandler
	public void onClone(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if (openers.contains(player.getUniqueId())) {
			invs.remove(player.getUniqueId());
			invs.put(player.getUniqueId(), event.getInventory());
			openers.remove(player.getUniqueId());
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = (Player) event.getPlayer();
		File file = new File(ItemManager.plugin.getDataFolder() + "/clothes", player.getUniqueId().toString());
		if (!file.exists())
			file.mkdir();
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		Inventory inv = invs.get(player.getUniqueId());
		for (int slot = 0; slot < inv.getSize(); slot++) {
			ItemStack item = inv.getItem(slot);
			if (item == null)
				continue;
			yaml.set(String.valueOf(slot), item);
		}
		try {
			yaml.save(file);
		} catch (IOException e) {
			System.out.println(player.getName() + "的衣物管理器保存出错！");
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = (Player) event.getPlayer();
		File file = new File(ItemManager.plugin.getDataFolder() + "/clothes", player.getUniqueId().toString());
		if (!file.exists())
			file.mkdir();
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		Inventory inv = Bukkit.createInventory(player, 9, ItemManager.items.get("ClothesContainer").displayName);
		for (String path : yaml.getKeys(false)) {
			ItemStack item = yaml.getItemStack(path);
			inv.setItem(Integer.valueOf(path), item);
		}
		invs.put(player.getUniqueId(), inv);
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (openers.contains(player.getUniqueId())) {
			if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
				event.setCancelled(true);
			if (event.getClickedInventory().getSize() != 9)
				return;
			ItemStack cursor = event.getCursor();
			if (cursor == null)
				return;
			if (cursor.getType() == Material.AIR)
				return;
			String type = cursor.getType().toString();
			if (type.contains("HELMET") || type.contains("CHESTPLATE") || type.contains("LEGGINGS")
					|| type.contains("BOOTS"))
				if (!ItemManager.isUTEItem(cursor).equalsIgnoreCase(""))
					return;
			player.sendMessage("只有可穿戴的UTE衣物可以放入衣物管理器！");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrag(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (openers.contains(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	public static Inventory getInventory(Player player) {
		return invs.get(player.getUniqueId());
	}
}
