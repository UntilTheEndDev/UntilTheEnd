package HamsterYDS.UntilTheEnd.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.logging.Level;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.cap.HudProvider;
import HamsterYDS.UntilTheEnd.internal.pdl.PlayerDataLoaderImpl;
import HamsterYDS.UntilTheEnd.player.role.IRole;
import HamsterYDS.UntilTheEnd.player.role.Roles;

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
		for (Player player : Bukkit.getOnlinePlayers())
			load(player);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		load(player);
		HudProvider.sanity.put(name, " ");
		HudProvider.humidity.put(name, " ");
		HudProvider.temperature.put(name, " ");
		HudProvider.tiredness.put(name, " ");
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
		HudProvider.tiredness.remove(name);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		IPlayer player = new IPlayer(37, 0, 200, 0.0);
		player.role = players.get(event.getEntity().getUniqueId()).role;
		player.roleStats = players.get(event.getEntity().getUniqueId()).roleStats;
		players.put(event.getEntity().getUniqueId(), player);
		save(event.getEntity());
		load(event.getEntity());
		event.getEntity().setMaxHealth(player.roleStats.healthMax);
	}

	public static void load(OfflinePlayer name) {
		int humidity = 0;
		int temperature = 37;
		int sanity = 200;
		int tiredness = 0;
		String roleName = "DEFAULT";
		int level = 0;
		int sanMax = 200;
		int healthMax = 20;
		double damageLevel = 1;

		try {
			final Map<String, Object> load = PlayerDataLoaderImpl.loader.load(playerdata, name);
			if (load != null) {
				humidity = ((Number) load.getOrDefault("humidity", 0)).intValue();
				temperature = ((Number) load.getOrDefault("temperature", 37)).intValue();
				sanity = ((Number) load.getOrDefault("sanity", 200)).intValue();
				tiredness = ((Number) load.getOrDefault("tiredness", 0)).intValue();
				roleName = ((String) load.getOrDefault("role", "DEFAULT"));
				level = ((Number) load.getOrDefault("level", 0)).intValue();
				sanMax = ((Number) load.getOrDefault("sanMax", 200)).intValue();
				healthMax = ((Number) load.getOrDefault("healthMax", 20)).intValue();
				damageLevel = ((Number) load.getOrDefault("damageLevel", 1)).intValue();
			}
		} catch (Throwable exception) {
			plugin.getLogger().log(Level.WARNING, "Failed to load " + name, exception);
		}
		IPlayer player = new IPlayer(temperature, humidity, sanity, tiredness);

		player.role = Roles.valueOf(roleName);
		player.roleStats = new IRole(level, sanMax, healthMax, damageLevel);

		players.put(name.getUniqueId(), player);
	}

	public static void save(OfflinePlayer name) {
		Map<String, Object> data = new HashMap<>();
		IPlayer player = players.get(name.getUniqueId());
		data.put("humidity", player.humidity);
		data.put("temperature", player.temperature);
		data.put("sanity", player.sanity);
		data.put("tiredness", player.tiredness);
		data.put("role", player.role.toString());
		data.put("level", player.roleStats.level);
		data.put("sanMax", player.roleStats.sanMax);
		data.put("healthMax", player.roleStats.healthMax);
		data.put("damageLevel", player.roleStats.damageLevel);
		try {
			PlayerDataLoaderImpl.loader.save(playerdata, name, data);
		} catch (IOException e) {
			plugin.getLogger().log(Level.WARNING, "Failed save data for " + name, e);
		}
	}

	public static double check(Player player, CheckType type) {
		if (!Config.enableWorlds.contains(player.getWorld())
				|| (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR))
			switch (type) {
			case TEMPERATURE:
				return 37;
			case HUMIDITY:
				return 0;
			case SANITY:
				return 200;
			case TIREDNESS:
				return 0;
			case DAMAGELEVEL:
				return 1;
			case HEALTHMAX:
				return 20;
			case LEVEL:
				return 0;
			case SANMAX:
				return 200;
			}
		IPlayer ip = players.get(player.getUniqueId());
		if (ip == null || type == null)
			return 1;
		switch (type) {
		case TEMPERATURE:
			return ip.temperature;
		case HUMIDITY:
			return ip.humidity;
		case SANITY:
			return ip.sanity;
		case TIREDNESS:
			return ip.tiredness;
		case DAMAGELEVEL:
			return ip.roleStats.damageLevel;
		case HEALTHMAX:
			return ip.roleStats.healthMax;
		case LEVEL:
			return ip.roleStats.level;
		case SANMAX:
			return ip.roleStats.sanMax;
		}
		return 0.0;
	}

	public static double check(Player name, String type) {
		return check(name, CheckType.search(type));
	}

	public enum CheckType {
		SANITY("san"), TEMPERATURE("tem"), HUMIDITY("hum"), TIREDNESS("tir"), SANMAX("sanmax"), HEALTHMAX(
				"healthmax"), LEVEL("level"), DAMAGELEVEL("damagelevel");
		private final String sname;

		public String getShortName() {
			return sname;
		}

		CheckType(String shorter) {
			this.sname = shorter;
		}

		public static CheckType search(String name) {
			if (name == null)
				return null;
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
			if (v.equalsIgnoreCase(mark))
				return " ";
			return v;
		};
	}

	public static void forgetChange(Player player, CheckType type, double counter) {
		if (player == null)
			return;
		if (type == null)
			return;
		IPlayer ip = players.get(player.getUniqueId());
		String mark;
		if (counter > 0)
			mark = "↑";
		else if (counter < 0)
			mark = "↓";
		else
			mark = " ";
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
			if (ip.temperature <= 10)
				player.sendTitle(UTEi18n.cache("mechanism.temperature.to-cool"), "");
			if (ip.temperature >= 60)
				player.sendTitle(UTEi18n.cache("mechanism.temperature.to-hot"), "");
			if (ip.temperature < -5)
				ip.temperature = -5;
			if (ip.temperature > 75)
				ip.temperature = 75;
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
			if (ip.humidity < 0)
				ip.humidity = 0;
			if (ip.humidity > 100)
				ip.humidity = 100;
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
			if (ip.sanity < 0)
				ip.sanity = 0;
			if (ip.sanity > ip.roleStats.sanMax)
				ip.sanity = ip.roleStats.sanMax;
			break;
		case TIREDNESS:
			ip.tiredness += counter;
			HudProvider.tiredness.put(player.getName(), mark);
			new BukkitRunnable() {
				@Override
				public void run() {
					HudProvider.tiredness.computeIfPresent(player.getName(), buildMarkFunc(mark));
				}
			}.runTaskLater(plugin, 40L);
			if (ip.tiredness < 0)
				ip.tiredness = 0;
			if (ip.tiredness > 100)
				ip.tiredness = 100;
			break;
		case DAMAGELEVEL:
			ip.roleStats.damageLevel += counter;
			break;
		case HEALTHMAX:
			ip.roleStats.healthMax += counter;
			break;
		case LEVEL:
			ip.roleStats.level += counter;
			break;
		case SANMAX:
			ip.roleStats.sanMax += counter;
			break;
		}
	}

	public static void change(Player player, CheckType type, double changement) {
		if (player == null)
			return;
		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
			return;
		if (player.isDead())
			return;
		forgetChange(player, type, changement);
	}

	public static void change(Player player, String type, double changement) {
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
		public double tiredness;
		public IRole roleStats;
		public Roles role;

		public IPlayer(int temperature, int humidity, int sanity, double tiredness) {
			this.temperature = temperature;
			this.humidity = humidity;
			this.sanity = sanity;
			this.tiredness = tiredness;
		}
	}
}
