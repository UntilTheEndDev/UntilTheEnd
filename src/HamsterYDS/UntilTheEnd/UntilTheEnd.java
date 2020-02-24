package HamsterYDS.UntilTheEnd;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.bossbar.BossBarAPI;

import HamsterYDS.UntilTheEnd.block.Block;
import HamsterYDS.UntilTheEnd.block.BlockManager;
import HamsterYDS.UntilTheEnd.cap.HudBar;
import HamsterYDS.UntilTheEnd.cap.hum.Humidity;
import HamsterYDS.UntilTheEnd.cap.san.Sanity;
import HamsterYDS.UntilTheEnd.cap.tem.Temperature;
import HamsterYDS.UntilTheEnd.crops.Crops;
import HamsterYDS.UntilTheEnd.food.Food;
import HamsterYDS.UntilTheEnd.guide.Guide;
import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.papi.UTEPapi;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.world.World;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class UntilTheEnd extends JavaPlugin{
	@Override public void onEnable() {
		loadConfig();
		new Config(this);
		new World(this);
		new Humidity(this);
		new Sanity(this);
		new Temperature(this);
		new HamsterYDS.UntilTheEnd.player.Player(this);
		new Crops(this);
		new Food(this);
		new Guide(this);
		new Block(this);
		for(Player player:Bukkit.getOnlinePlayers()) {
			PlayerManager.load(player.getName());
		}
		if(Bukkit.getPluginManager().isPluginEnabled("BossBarAPI")) new HudBar(this);
		new ItemLoader(this);
		new Commands(this);
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) 
			new UTEPapi(this).hook();
	}
	@Override public void onDisable() {
		for(Player player:Bukkit.getOnlinePlayers()) {
			PlayerManager.save(player.getName());
			BossBarAPI.removeAllBars(player);
		}
		BlockManager.saveBlocks();
	}
	public void loadConfig() {
		saveResource("config.yml", false);
		String language=getConfig().getString("language");
		saveResource(language, false);
	}
}
