package HamsterYDS.UntilTheEnd;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

import HamsterYDS.UntilTheEnd.cap.HudBossBar;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
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
import HamsterYDS.UntilTheEnd.cap.tir.Tiredness;
import HamsterYDS.UntilTheEnd.crops.Crops;
import HamsterYDS.UntilTheEnd.food.Food;
import HamsterYDS.UntilTheEnd.guide.Guide;
import HamsterYDS.UntilTheEnd.internal.pdl.PlayerDataLoaderImpl;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.nms.NMSManager;
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
    public static boolean DEBUG;

    public static UntilTheEnd getInstance() {
        return INSTANCE;
    }

    public UntilTheEnd() {
        INSTANCE = this;
    }

    private boolean failedLoading;

    @Override
    public void onEnable() {
        getLogger().setLevel((DEBUG = getConfig().getBoolean("debug", false)) ? Level.ALL : Level.INFO);
        if (DEBUG) {
            String warn = "" +
                    "#\n" +
                    "# WARNING     WARNING  #\n" +
                    "# You are enter the DEBUG mode. UntilTheEnd will output a lot of internal running logs.\n" +
                    "#\n" +
                    "# If you do not disable the file system output in Log4j configuration,\n" +
                    "# it will cause a lot of unnecessary content in the log files.\n" +
                    "#\n" +
                    "# If you are not a developer / required to enable DEBUG mode,\n" +
                    "# disable it in UTE config.\n" +
                    "#\n" +
                    "# Copyright (c) 2018-2020 Karlatemp, 2020 南外丶仓鼠, All rights reserved.\n" +
                    "#\n" +
                    "#          UTE Developer Team";
            int start = 0;
            do {
                int next = warn.indexOf('\n', start);
                if (next == -1) {
                    getLogger().warning(warn.substring(start));
                    break;
                } else {
                    getLogger().warning(warn.substring(start, next));
                    start = next + 1;
                }
            } while (true);
        }
        failedLoading = false;
        try {
            int pluginId = 6586;
            Metrics metrics = new Metrics(this);
            metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
            checkUpdate();
            loadConfig();
            new NMSManager();
            new Config(this);
            new World(this);
            new Temperature(this);
            new Sanity(this);
            new Humidity(this);
            new Tiredness(this);
            new Guide(this);
            new Crops(this);
            new Block(this);
            new ItemManager(this);
            new HamsterYDS.UntilTheEnd.player.Player(this);
            new HudProvider(this);
            new Food(this);
            new Commands(this);
            new UTEExpansion().register();
            getLogger().log(Level.INFO, UTEi18n.parse("logging.store.type", PlayerDataLoaderImpl.loader.getClass().getSimpleName(), getConfig().getString("saving")));
        } catch (Throwable throwable) {
            failedLoading = true;
            getLogger().log(Level.SEVERE, "Failed to initialize UntilTheEnd!", throwable);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (failedLoading) return;
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
                Bukkit.getConsoleSender().sendMessage(UTEi18n.cacheWithPrefix("logging.update.checking"));
                Bukkit.getConsoleSender().sendMessage(UTEi18n.cache("prefix") + UTEi18n.parse("logging.update.current", getDescription().getVersion()));
                latestVersion = getLatestVersion();
                if (latestVersion == null) {
                    return;
                }
                if (latestVersion.equalsIgnoreCase(getDescription().getVersion())) {
                    getServer().getConsoleSender().sendMessage(UTEi18n.cacheWithPrefix("logging.update.latest"));
                } else {
                    getServer().getConsoleSender().sendMessage(UTEi18n.cacheWithPrefix("logging.update.update"));
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
                    player.sendMessage(UTEi18n.cacheWithPrefix("logging.update.for-player"));
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
