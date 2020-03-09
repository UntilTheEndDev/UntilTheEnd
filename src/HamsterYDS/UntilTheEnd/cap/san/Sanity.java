package HamsterYDS.UntilTheEnd.cap.san;

import java.io.File;

import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Sanity {
    public static YamlConfiguration yaml;

    public Sanity(UntilTheEnd plugin) {
        yaml = Config.autoUpdateConfigs("sanity.yml");
        Bukkit.getConsoleSender().sendMessage(UTEi18n.cacheWithPrefix("cap.san.provider.loading"));
        SanityProvider.loadAura();
        new ChangeTasks(plugin);
        new InfluenceTasks(plugin);
        plugin.getServer().getPluginManager().registerEvents(new InfluenceEvents(), plugin);
    }
}
