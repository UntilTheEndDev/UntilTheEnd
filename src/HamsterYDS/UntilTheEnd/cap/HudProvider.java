package HamsterYDS.UntilTheEnd.cap;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

public class HudProvider {
    public static UntilTheEnd plugin;
    public static YamlConfiguration yaml;
    public static List<String> lines;
    public static HashMap<UUID, String> sanity = new HashMap<>();
    public static HashMap<UUID, String> humidity = new HashMap<>();
    public static HashMap<UUID, String> temperature = new HashMap<>();
    public static HashMap<UUID, String> tiredness = new HashMap<>();

    public HudProvider(UntilTheEnd plugin) {
        plugin.saveResource("hud.yml", false);
        File file = new File(plugin.getDataFolder(), "hud.yml");
        yaml = YamlConfiguration.loadConfiguration(file);
        lines = yaml.getStringList("scoreboard.list");
        if (yaml.getBoolean("bar.enable"))
            new HudBossBar().runTaskTimer(plugin, 0L, yaml.getLong("fresh"));
    }
}
