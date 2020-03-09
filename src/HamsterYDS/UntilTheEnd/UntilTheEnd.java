package HamsterYDS.UntilTheEnd;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

import HamsterYDS.UntilTheEnd.cap.HudBossBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.block.Block;
import HamsterYDS.UntilTheEnd.block.BlockManager;
import HamsterYDS.UntilTheEnd.cap.HudProvider;
import HamsterYDS.UntilTheEnd.cap.hum.Humidity;
import HamsterYDS.UntilTheEnd.cap.san.Sanity;
import HamsterYDS.UntilTheEnd.cap.tem.Temperature;
import HamsterYDS.UntilTheEnd.cap.tiredness.Tiredness;
import HamsterYDS.UntilTheEnd.crops.Crops;
import HamsterYDS.UntilTheEnd.food.Food;
import HamsterYDS.UntilTheEnd.guide.Guide;
import HamsterYDS.UntilTheEnd.internal.pdl.PlayerDataLoaderImpl;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.papi.UTEExpansion;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.world.World;
import HamsterYDS.UntilTheEnd.world.WorldProvider;
import me.clip.placeholderapi.metrics.bukkit.Metrics;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class UntilTheEnd extends JavaPlugin implements Listener {
    String latestVersion;
    boolean isLatest = true;
    private static UntilTheEnd INSTANCE;

    public static UntilTheEnd getInstance() {
        return INSTANCE;
    }

    public UntilTheEnd() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        int pluginId = 6586;
        Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
        checkUpdate();
        loadConfig();
        new Config(this);
        new World(this);
        new Temperature(this);
        new Sanity(this);
        new Humidity(this);
        new Tiredness(this);
        //new Tiredness(this);
        new Guide(this);
        new Crops(this);
        new HamsterYDS.UntilTheEnd.player.Player(this);
        new HudProvider(this);
        new Food(this);
        new Block(this);
        new ItemManager(this);
        new Commands(this);
        new UTEExpansion().register();
        getLogger().log(Level.INFO, "数据存储模式: " + PlayerDataLoaderImpl.loader.getClass().getSimpleName() + "[" + getConfig().getString("saving") + "]");
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerManager.save(player);
            HudBossBar.release(player.getUniqueId());
        }
        WorldProvider.saveWorlds();
        BlockManager.saveBlocks();
    }

    public void loadConfig() {
        File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists())
            saveResource("config.yml", true);
        String language = getConfig().getString("language");
        File langFile = new File(this.getDataFolder(), language);
        if (!langFile.exists())
            saveResource(language, false);
    }

    public void checkUpdate() {
        new BukkitRunnable() {
            public void run() {
                Bukkit.getConsoleSender().sendMessage("[UntilTheEnd]插件检查更新中......");
                Bukkit.getConsoleSender().sendMessage("[UntilTheEnd]插件当前版本为>>" + getDescription().getVersion());
                latestVersion = getLatestVersion();
                if (latestVersion == null) {
                    return;
                }
                if (latestVersion.equalsIgnoreCase(getDescription().getVersion())) {
                    getLogger().info("您的插件已经是最新版啦！");
                } else {
                    getLogger().info("您的插件不是最新版，请立即更新！");
                    isLatest = false;
                    Bukkit.getOnlinePlayers().forEach(this::sendUpdate);
                    Bukkit.getPluginManager().registerEvents(new Listener() {
                        @EventHandler
                        public void onPlayerJoin(PlayerJoinEvent event) {
                            sendUpdate(event.getPlayer());
                        }
                    }, UntilTheEnd.this);
                }
            }

            private void sendUpdate(Player player) {
                if (player.hasPermission("ute.update"))
                    player.sendMessage("§6§l[UntilTheEnd]§4§l您的插件不是最新版，很有可能有BUG！");
            }
        }.runTaskAsynchronously(this);
    }

    public static String getLatestVersion() {
        HttpURLConnection connection = null;
        try {
            int timeout = 5000;
            URL url = new URL("https://untiltheend.coding.net/p/UntilTheEnd/d/UntilTheEnd/git/raw/master/UTEversion.txt");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(timeout);
            InputStream inStream = connection.getInputStream();
            final StringBuilder builder = new StringBuilder(255);
            int byteRead;
            while ((byteRead = inStream.read()) != -1) {
                builder.append((char) byteRead);
            }
            return builder.toString();
        } catch (Exception exception) {
            INSTANCE.getLogger().log(Level.WARNING, "Failed to get update info.", exception);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null; //这个return代表获取不到版本时返回的字符串，当然你可以改别的
    }
}
