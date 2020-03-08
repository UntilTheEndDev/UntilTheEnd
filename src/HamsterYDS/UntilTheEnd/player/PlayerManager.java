package HamsterYDS.UntilTheEnd.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.logging.Level;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.internal.pdl.PlayerDataLoaderImpl;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.cap.HudProvider;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class PlayerManager implements Listener {
    public static UntilTheEnd plugin = UntilTheEnd.getInstance();
    private static HashMap<UUID, IPlayer> players = new HashMap<>();
    public static final File playerdata = new File(plugin.getDataFolder(), "playerdata");

    public PlayerManager() {
    }

    public PlayerManager(UntilTheEnd plugin) {
        new SavingTask();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        for (Player player : Bukkit.getOnlinePlayers()) load(player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        load(player);
        HudProvider.sanity.put(name, " ");
        HudProvider.humidity.put(name, " ");
        HudProvider.temperature.put(name, " ");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        save(player);
        players.remove(player.getUniqueId());
        HudProvider.sanity.remove(name);
        HudProvider.humidity.remove(name);
        HudProvider.temperature.remove(name);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        IPlayer player = new IPlayer(37, 0, 200);
        players.put(event.getEntity().getUniqueId(), player);
    }

    public static void load(OfflinePlayer name) {
        int humidity = 0;
        int temperature = 37;
        int sanity = 200;
        try {
            final Map<String, Object> load = PlayerDataLoaderImpl.loader.load(playerdata, name);
            if (load != null) {
                humidity = ((Number) load.get("humidity")).intValue();
                temperature = ((Number) load.get("temperature")).intValue();
                sanity = ((Number) load.get("sanity")).intValue();
            }
        } catch (Throwable exception) {
            plugin.getLogger().log(Level.WARNING, "Failed to load " + name, exception);
        }
        IPlayer player = new IPlayer(temperature, humidity, sanity);
        players.put(name.getUniqueId(), player);
    }

    public static void save(OfflinePlayer name) {
        Map<String, Object> data = new HashMap<>();
        IPlayer player = players.get(name.getUniqueId());
        data.put("humidity", player.humidity);
        data.put("temperature", player.temperature);
        data.put("sanity", player.sanity);
        try {
            PlayerDataLoaderImpl.loader.save(playerdata, name, data);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed save data for " + name, e);
        }
    }

    public static int check(Player name, CheckType type) {
        if (!Config.enableWorlds.contains(name.getWorld()))
            switch (type) {
                case TEMPERATURE:
                    return 37;
                case HUMIDITY:
                    return 0;
                case SANITY:
                    return 200;
                default:
                    return 1;
            }
        IPlayer player = players.get(name.getUniqueId());
        if (player == null || type == null) return 1;
        switch (type) {
            case TEMPERATURE:
                return player.temperature;
            case HUMIDITY:
                return player.humidity;
            case SANITY:
                return player.sanity;
            default:
                return 1;
        }
    }

    public static int check(Player name, String type) {
        return check(name, CheckType.search(type));
    }

    public enum CheckType {
        SANITY("san"), TEMPERATURE("tem"), HUMIDITY("hum");
        private final String sname;

        public String getShortName() {
            return sname;
        }

        CheckType(String shorter) {
            this.sname = shorter;
        }

        public static CheckType search(String name) {
            if (name == null) return null;
            CheckType[] val = values();
            for (CheckType c : val) {
                if (c.sname.equalsIgnoreCase(name) || c.name().equalsIgnoreCase(name))
                    return c;
            }
            return null;
        }
    }

    private static BiFunction<String, String, String> buildMarkFunc(String mark) {
        return (k, v) -> {
            if (v.equalsIgnoreCase(mark)) return " ";
            return v;
        };
    }

    public static void forgetChange(Player player, CheckType type, int counter) {
        if (player == null) return;
        if (type == null) return;
        IPlayer ip = players.get(player.getUniqueId());
        String mark;
        if (counter > 0) mark = "↑";
        else if (counter < 0) mark = "↓";
        else mark = " ";
        switch (type) {
            case TEMPERATURE:
                ip.temperature += counter;
                HudProvider.temperature.put(player.getName(), mark);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        HudProvider.temperature.computeIfPresent(player.getName(), buildMarkFunc(mark));
                    }
                }.runTaskLater(plugin, 40L);
                if (ip.temperature <= 10) player.sendTitle("§9太冷了！", "");
                if (ip.temperature >= 60) player.sendTitle("§9太热了！", "");
                if (ip.temperature < -5) ip.temperature = -5;
                if (ip.temperature > 75) ip.temperature = 75;
                break;
            case HUMIDITY:
                ip.humidity += counter;
                HudProvider.humidity.put(player.getName(), mark);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        HudProvider.humidity.computeIfPresent(player.getName(), buildMarkFunc(mark));
                    }
                }.runTaskLater(plugin, 40L);
                if (ip.humidity < 0) ip.humidity = 0;
                if (ip.humidity > 100) ip.humidity = 100;
                break;
            case SANITY:
                ip.sanity += counter;
                HudProvider.sanity.put(player.getName(), mark);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        HudProvider.sanity.computeIfPresent(player.getName(), buildMarkFunc(mark));
                    }
                }.runTaskLater(plugin, 40L);
                if (ip.sanity < 0) ip.sanity = 0;
                if (ip.sanity > 200) ip.sanity = 200;
                break;
        }
    }

    public static void change(Player player, CheckType type, int changement) {
        if (player == null)
            return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
            return;
        if (player.isDead()) return;
        forgetChange(player, type, changement);
    }

    public static void change(Player player, String type, int changement) {
        change(player, CheckType.search(type), changement);
    }

    private static class SavingTask extends BukkitRunnable {
        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers())
                save(player);
        }

        public SavingTask() {
            runTaskTimer(plugin, 0L, plugin.getConfig().getInt("player.stats.autosave") * 20);
        }
    }

    private static class IPlayer {
        public int temperature;
        public int humidity;
        public int sanity;

        public IPlayer(int temperature, int humidity, int sanity) {
            this.temperature = temperature;
            this.humidity = humidity;
            this.sanity = sanity;
        }
    }
}
