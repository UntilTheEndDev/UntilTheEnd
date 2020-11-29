package ute.internal.pdl;

import ute.UntilTheEnd;
import ute.api.spi.PlayerDataLoader;

/**
 * Create at 2020/3/8 0:07
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.internal.pdl
 */
public class PlayerDataLoaderImpl {
    public static PlayerDataLoader loader;

    static {
        switch (UntilTheEnd.getInstance().getConfig().getString("saving", "name")) {
            case "nameV2":
                loader = new PlayerNameYmlV2();
                break;
            case "uuid":
                loader = new PlayerUUIDYml();
                break;
            case "uuid-bin":
                loader = new UUIDBin();
                break;
            case "name-bin":
                loader = new NameBin();
                break;
            case "name":
            default:
                loader = new PlayerNameYml();
        }
    }
}
