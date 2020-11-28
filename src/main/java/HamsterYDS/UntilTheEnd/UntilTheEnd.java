package HamsterYDS.UntilTheEnd;

import HamsterYDS.UntilTheEnd.api.UTEPapiExpansion;
import HamsterYDS.UntilTheEnd.cap.HudBossBar;
import HamsterYDS.UntilTheEnd.cap.HudProvider;
import HamsterYDS.UntilTheEnd.cap.hum.Humidity;
import HamsterYDS.UntilTheEnd.cap.san.Sanity;
import HamsterYDS.UntilTheEnd.cap.tem.Temperature;
import HamsterYDS.UntilTheEnd.cap.tir.Tiredness;
import HamsterYDS.UntilTheEnd.crops.Crops;
import HamsterYDS.UntilTheEnd.food.Food;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.internal.BuildData;
import HamsterYDS.UntilTheEnd.internal.DataConverter;
import HamsterYDS.UntilTheEnd.internal.Metrics;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import HamsterYDS.UntilTheEnd.internal.pdl.PlayerDataLoaderImpl;
import HamsterYDS.UntilTheEnd.item.BlockManager;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.nms.ActionBarManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.world.World;
import HamsterYDS.UntilTheEnd.world.WorldProvider;
import HamsterYDS.UntilTheEnd.world.cave.CaveManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class UntilTheEnd extends JavaPlugin implements Listener {
    String latestVersion;
    boolean isLatest = true;
    private static UntilTheEnd INSTANCE;
    public static boolean DEBUG;

    public static @NotNull UntilTheEnd getInstance() {
        return INSTANCE;
    }

    public UntilTheEnd() {
        INSTANCE = this;
    }

    private boolean failedLoading;

    @Override
    public void onEnable() {
        Logging.getLogger().setLevel((DEBUG = getConfig().getBoolean("debug", false)) ? Level.ALL : Level.INFO);
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
            // Pre starting check
            {
                Logger logger = Logging.getLogger();
                logger.info("UntilTheEnd - build version:   " + BuildData.GIT_COMMIT);
                logger.info("UntilTheEnd - build time:      " + new Date(BuildData.BUILD_TIME));
                logger.info("UntilTheEnd - build timestamp: " + BuildData.BUILD_TIME);
                logger.info("UntilTheEnd - builder:         " + BuildData.BUILDER);
                BuildData.checkSystem();
            }

            int pluginId = 6586;
            Metrics metrics = new Metrics(this, pluginId);
            metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
            checkUpdate();
            loadConfig();
            DataConverter.run();
            try {
                ActionBarManager.initialize();
            } catch (Throwable exception) {
                getLogger().log(Level.WARNING, "Failed to initialize ActionBar Manager", exception);
            }
            Config.initialize();
            CaveManager.initialize();
            World.initialize(this);
            Temperature.initialize(this);
            Sanity.initialize(this);
            Humidity.initialize(this);
            Tiredness.initialize(this);
            CraftGuide.init();
            Crops.initialize(this);
            ItemManager.initialize(this);
            HamsterYDS.UntilTheEnd.player.Player.initialize(this);
            HudProvider.initialize(this);
            new Food(this);
            new Commands(this);
            try {
                Class.forName("me.clip.placeholderapi.expansion.PlaceholderExpansion");
                new UTEPapiExpansion().register();
            } catch (ClassNotFoundException ignored) {
            }
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

    private void checkUpdate() {
        String version = getDescription().getVersion().toLowerCase();
        if (version.contains("dev")) {
            String msg = UTEi18n.cache("prefix") + "§c # WARMING: You are using Development version! This may not be supported in version!";
            Bukkit.getConsoleSender().sendMessage(msg);
            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void onPlayerLogin(PlayerLoginEvent event) {
                    Player player = event.getPlayer();
                    if (player.hasPermission("ute.update")) {
                        player.sendMessage(msg);
                    }
                }
            }, this);
            return;
        }
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
                        @EventHandler()
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
            final StringBuilder buffer = new StringBuilder(255);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                char[] buffer0 = new char[255];
                while (true) {
                    int length = reader.read(buffer0);
                    if (length == -1) break;
                    buffer.append(buffer0, 0, length);
                }
            }
            return buffer.toString().trim();
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
