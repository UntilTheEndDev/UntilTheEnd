package ute.cap.san;

import ute.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import ute.Config;
import ute.UntilTheEnd;

public class Sanity {
    public static YamlConfiguration yaml;

    public static void initialize(UntilTheEnd plugin) {
        yaml = Config.autoUpdateConfigs("sanity.yml");
        Bukkit.getConsoleSender().sendMessage(UTEi18n.cacheWithPrefix("cap.san.provider.loading"));
        SanityProvider.loadAura();
        ChangeTasks.initialize();
        InfluenceTasks.initialize();
        plugin.getServer().getPluginManager().registerEvents(new InfluenceEvents(), plugin);
    }
}
