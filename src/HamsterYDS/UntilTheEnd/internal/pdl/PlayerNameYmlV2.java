package HamsterYDS.UntilTheEnd.internal.pdl;

import HamsterYDS.UntilTheEnd.api.spi.PlayerDataLoader;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Create at 2020/3/8 0:08
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.internal.pdl
 */
public class PlayerNameYmlV2 implements PlayerDataLoader {
    @Override
    public Map<String, Object> load(File playerdata, OfflinePlayer player) {
        File file = new File(playerdata, player.getName() + ".yml");
        if (!file.isFile()) return null;
        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        return configuration.getValues(false);
    }

    @Override
    public void save(File playerdata, OfflinePlayer player, Map<String, Object> data) throws IOException {
        File file = new File(playerdata, player.getName() + ".yml");
        final YamlConfiguration configuration = new YamlConfiguration();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            configuration.set(entry.getKey(), entry.getValue());
        }
        configuration.save(file);
    }
}
