package ute.cap;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import ute.Config;
import ute.UntilTheEnd;

public class HudProvider {
    public static YamlConfiguration yaml;
    public static HashMap<UUID, String> sanity = new HashMap<>();
    public static HashMap<UUID, String> humidity = new HashMap<>();
    public static HashMap<UUID, String> temperature = new HashMap<>();
    public static HashMap<UUID, String> tiredness = new HashMap<>();

    public static void initialize(UntilTheEnd plugin) {
        yaml = Config.autoUpdateConfigs("hud.yml");
        if (yaml.getBoolean("bar.enable"))
            new HudBossBar().runTaskTimer(plugin, 0L, yaml.getLong("fresh"));
    }
}
