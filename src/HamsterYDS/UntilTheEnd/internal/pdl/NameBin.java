package HamsterYDS.UntilTheEnd.internal.pdl;

import org.bukkit.OfflinePlayer;

import java.io.File;

/**
 * Create at 2020/3/8 0:28
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.internal.pdl
 */
public class NameBin extends BinStore {
    @Override
    protected File openFile(File playerData, OfflinePlayer player) {
        return new File(playerData, player.getName() + ".bin");
    }
}
