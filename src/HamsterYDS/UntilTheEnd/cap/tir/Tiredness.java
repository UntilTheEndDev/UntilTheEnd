package HamsterYDS.UntilTheEnd.cap.tir;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;

public class Tiredness {
    public static YamlConfiguration yaml;

    public Tiredness(UntilTheEnd plugin) {
        yaml = Config.autoUpdateConfigs("tiredness.yml");
        Bukkit.getConsoleSender().sendMessage(UTEi18n.cacheWithPrefix("cap.tir.provider.loading"));
        new ChangeTasks(plugin);
        new InfluenceTasks().runTaskTimer(plugin, 0L, 20L);
        plugin.getServer().getPluginManager().registerEvents(new ChangeEvents(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InfluenceEvents(plugin), plugin);
    }
}
