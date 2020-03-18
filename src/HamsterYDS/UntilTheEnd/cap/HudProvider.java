package HamsterYDS.UntilTheEnd.cap;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

public class HudProvider {
    public static UntilTheEnd plugin;
    public static YamlConfiguration yaml;
    public static HashMap<UUID, String> sanity = new HashMap<>();
    public static HashMap<UUID, String> humidity = new HashMap<>();
    public static HashMap<UUID, String> temperature = new HashMap<>();
    public static HashMap<UUID, String> tiredness = new HashMap<>();

    public HudProvider(UntilTheEnd plugin) {
        yaml =Config.autoUpdateConfigs("hud.yml");
        if (yaml.getBoolean("bar.enable"))
            new HudBossBar().runTaskTimer(plugin, 0L, yaml.getLong("fresh"));
    }
}
