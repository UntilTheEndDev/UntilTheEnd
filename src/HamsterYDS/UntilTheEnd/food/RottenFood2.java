package HamsterYDS.UntilTheEnd.food;

import java.io.File;
import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.block.BlockManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class RottenFood2 implements Listener{
	public static UntilTheEnd plugin;
	int cd=0;
	public RottenFood2(UntilTheEnd plugin) {
		this.plugin=plugin;
		cd=plugin.getConfig().getInt("food.rotten.speed");
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	@EventHandler public void onClose(InventoryCloseEvent event) {
		Inventory inv=event.getInventory();
		if(inv.getType()==InventoryType.PLAYER) return;
		if(inv.getType()==InventoryType.CRAFTING) return;
		File file=new File(plugin.getDataFolder()+"/data/","chestfoods.yml");
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		Location loc=inv.getLocation();
		if(loc==null) return;
		if(loc.getBlock().getType()==Material.AIR) return;
		World world=loc.getWorld();
		yaml.set(world.getName()+"-"+loc.getBlockX()+"-"+loc.getBlockY()+"-"+loc.getBlockZ(),world.getFullTime());
		try {
			yaml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@EventHandler public void onOpen(InventoryOpenEvent event) {
		Inventory inv=event.getInventory();
		Location loc=inv.getLocation();
		if(loc==null) return;
		World world=loc.getWorld();
		String toString=world.getName()+"-"+loc.getBlockX()+"-"+loc.getBlockY()+"-"+loc.getBlockZ();
		boolean flag=false;
		if(BlockManager.blocks.containsKey(toString)) {
			String name=BlockManager.blocks.get(toString);
			if(name.equalsIgnoreCase("Refridgerator")) 
				flag=true;
		}
		File file=new File(plugin.getDataFolder()+"/data/","chestfoods.yml");
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		long fullTime=yaml.getLong(toString);
		long fullTimeNow=world.getFullTime();
		int times=(int) ((fullTimeNow-fullTime)/cd/20);
		for(int slot=0;slot<inv.getSize();slot++) {
			ItemStack item=inv.getItem(slot);
			if(item==null) return;
			if(item.getType().isEdible()) 
				if(flag) inv.setItem(slot,RottenFood1.setRotten(item,times/4));
				else inv.setItem(slot,RottenFood1.setRotten(item,times));
		}
	}
}
