package HamsterYDS.UntilTheEnd.cap.hum;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Moistness extends BukkitRunnable implements Listener{
	public static UntilTheEnd plugin;
	public static HashMap<Material,Material> moistness=new HashMap<Material,Material>();
	public Moistness(UntilTheEnd plugin) {
		this.plugin=plugin;
		loadConfig();
		runTaskTimer(plugin,0L,150L);
	}
	public static void loadConfig() {
		File file=new File(plugin.getDataFolder(),"moistness.yml");
		plugin.saveResource("moistness.yml",false);
		YamlConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		for(String path:yaml.getKeys(false)) {
			Material material=Material.getMaterial(path);
			Material newMaterial=Material.getMaterial(yaml.getString(path));
			moistness.put(material,newMaterial);
		}
	}
	@Override
	public void run() {
		for(Player player:Bukkit.getOnlinePlayers()) {
			int hum=PlayerManager.check(player.getName(),"hum");
			if(hum<10) continue;
			goWet(player,hum/10);
		}
	}
	public static void goWet(Player player,int level) {
		PlayerInventory inv=player.getInventory();
		for(int slot=0;slot<=60;slot++) {
			ItemStack item=inv.getItem(slot);
			if(item==null) continue;
			Material material=item.getType();
			if(!moistness.containsKey(material)) continue;
			Material newMaterial=moistness.get(material);
			if(Math.random()*level>=0.95) {
				item.setType(newMaterial);
				inv.setItem(slot,item);
			}
		}
	}
}
