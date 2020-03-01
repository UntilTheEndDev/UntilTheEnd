package HamsterYDS.UntilTheEnd;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
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
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.papi.UTEPapi;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.world.World;
import me.clip.placeholderapi.metrics.bukkit.Metrics;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class UntilTheEnd extends JavaPlugin implements Listener{
	String latestVersion;
	boolean isLatest=true;
	@Override public void onEnable() {
		Metrics metrics = new Metrics(this);
		metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
		Bukkit.getPluginManager().registerEvents(this,this);
		checkUpdate();
		loadConfig();
		new Config(this);
		new World(this);
	  //固有属性重构完毕
		new Temperature(this);
		new Sanity(this);
		new Humidity(this);
	  //new Tiredness(this);
		new Guide(this);
		new Crops(this);
		new HamsterYDS.UntilTheEnd.player.Player(this);
	  //未重构
		new Food(this);
		new Block(this);
		new HudBar(this);
		new ItemManager(this);
		new Commands(this);
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new UTEPapi(this).hook();
		}
	}
	@Override public void onDisable() {
		for(Player player:Bukkit.getOnlinePlayers()) {
			BossBarAPI.removeAllBars(player);
			PlayerManager.save(player.getName());
		}
		BlockManager.saveBlocks();
	}
	public void loadConfig() {
		File file=new File(this.getDataFolder(),"config.yml");
		if(!file.exists())
			saveResource("config.yml",true);
		String language=getConfig().getString("language");
		File langFile=new File(this.getDataFolder(),language);
		if(!langFile.exists())	
			saveResource(language, false);
	}
	@EventHandler public void onJoin(PlayerJoinEvent event) {
		if(!isLatest)
			event.getPlayer().sendMessage("§6§l[UntilTheEnd]§4§l您的插件不是最新版，很有可能有BUG！");
	}
	public void checkUpdate() {
		int pluginId=6586;
        Metrics metrics=new Metrics(this);
		Bukkit.getConsoleSender().sendMessage("[UntilTheEnd]插件检查更新中......");
		System.out.println("[UntilTheEnd]插件当前版本为>>"+getDescription().getVersion());
		latestVersion=getLatestVersion();
		System.out.println(latestVersion);
		new BukkitRunnable() {
			public void run() {
				if(latestVersion.equalsIgnoreCase(getDescription().getVersion())) {
					getLogger().info("您的插件已经是最新版啦！");
				}else {
					getLogger().info("您的插件不是最新版，请立即更新！");
					isLatest=false;
				}
			}
		}.runTaskAsynchronously(this);
	}
	public static String getLatestVersion() {
		HttpURLConnection connection = null;
		try {
			int timeout=5000;
			URL url=new URL("https://untiltheend.coding.net/p/UntilTheEnd/d/UntilTheEnd/git/raw/master/UTEversion.txt");
		    connection=(HttpURLConnection)url.openConnection();
		    connection.setConnectTimeout(timeout);
		    InputStream inStream=connection.getInputStream();
		    final StringBuilder builder=new StringBuilder(255);
		    int byteRead;
		    while ((byteRead=inStream.read())!=-1) {
		    	builder.append((char) byteRead);
		    }
		    String response=builder.toString();
		    return response;
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (connection!=null) {
				connection.disconnect();
			}
		}
		  return "无法获取"; //这个return代表获取不到版本时返回的字符串，当然你可以改别的
	}
}
