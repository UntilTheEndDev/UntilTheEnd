package HamsterYDS.UntilTheEnd.cap;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

public class HudProvider {
    public static UntilTheEnd plugin;
    public static YamlConfiguration yaml;
    public static List<String> lines;
    public static HashMap<String, String> sanity = new HashMap<String, String>();
    public static HashMap<String, String> humidity = new HashMap<String, String>();
    public static HashMap<String, String> temperature = new HashMap<String, String>();
    public static HashMap<String, String> tiredness = new HashMap<String, String>();

    public HudProvider(UntilTheEnd plugin) {
        plugin.saveResource("hud.yml", false);
        File file = new File(plugin.getDataFolder(), "hud.yml");
        yaml = YamlConfiguration.loadConfiguration(file);
        lines = yaml.getStringList("scoreboard.list");
        if (yaml.getBoolean("bar.enable"))
            new HudBossBar().runTaskTimer(plugin, 0L, yaml.getLong("fresh"));
    }
}
