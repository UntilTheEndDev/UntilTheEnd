package ute.api.spi;

import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Create at 2020/3/8 0:04
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.api.spi
 */
public interface PlayerDataLoader {
    Map<String, Object>
    load(File playerdata, OfflinePlayer player) throws IOException;

    void save(File playerdata,
              OfflinePlayer player,
              Map<String, Object> data) throws IOException;
}
