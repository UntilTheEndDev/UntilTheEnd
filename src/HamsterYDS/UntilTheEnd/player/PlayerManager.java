package HamsterYDS.UntilTheEnd.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

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

    public static int check(OfflinePlayer name, String type) {
        IPlayer player = players.get(name.getUniqueId());
        if (player == null) return 1;
        switch (type) {
            case "tem":
                return player.temperature;
            case "hum":
                return player.humidity;
            case "san":
                return player.sanity;
            default:
                return 1;
        }
    }

    public static void change(Player name, String type, int changement) {
        if (name == null)
            return;
        if (name.getGameMode() == GameMode.CREATIVE || name.getGameMode() == GameMode.SPECTATOR)
            return;
        IPlayer player = players.get(name.getUniqueId());
        String mark = "";
        if (changement > 0) mark = "↑";
        if (changement < 0) mark = "↓";
        if (changement == 0) mark = " ";
        switch (type) {
            case "tem": {
                player.temperature += changement;
                HudProvider.temperature.remove(name.getName());
                HudProvider.temperature.put(name.getName(), mark);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        HudProvider.temperature.put(name.getName(), "");
                        cancel();
                    }
                }.runTaskTimer(plugin, 40L, 20L);
                if (player.temperature <= 10) name.sendTitle("§9太冷了！", "");
                if (player.temperature >= 60) name.sendTitle("§9太热了！", "");
                if (player.temperature < -5) player.temperature = -5;
                if (player.temperature > 75) player.temperature = 75;
                break;
            }
            case "hum": {
                player.humidity += changement;
                HudProvider.humidity.put(name.getName(), mark);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        HudProvider.humidity.put(name.getName(), "");
                        cancel();
                    }
                }.runTaskTimer(plugin, 40L, 20L);
                if (player.humidity < 0) player.humidity = 0;
                if (player.humidity > 100) player.humidity = 100;
                break;
            }
            case "san": {
                player.sanity += changement;
                HudProvider.sanity.put(name.getName(), mark);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        HudProvider.sanity.put(name.getName(), "");
                        cancel();
                    }
                }.runTaskTimer(plugin, 40L, 20L);
                if (player.sanity < 0) player.sanity = 0;
                if (player.sanity > 200) player.sanity = 200;
                break;
            }
        }
        players.put(name.getUniqueId(), player);
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
