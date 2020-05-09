/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/05/09 19:55:20
 *
 * until-the-end/until-the-end.main/DataConverter.java
 */

package HamsterYDS.UntilTheEnd.internal;

import HamsterYDS.UntilTheEnd.Logging;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.BlockApi;
import HamsterYDS.UntilTheEnd.api.Loc3D;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataConverter {
    public static void run() {
        final UntilTheEnd plugin = UntilTheEnd.getInstance();
        final Logger logger = Logging.getLogger();
        logger.finer("[DataConverter] Task start......");
        File data = new File(plugin.getDataFolder(), "data");
        resolve(new File(data, "blocks.yml"), "Blocks", logger);
        resolve(new File(data, "traps.yml"), "Traps", logger);
    }

    private static void resolve(File blocks, String name, Logger logger) {
        logger.finer("[DataConverter] Resolve " + name + "...");
        if (blocks.isFile()) {
            final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(blocks);
            YamlConfiguration current = new YamlConfiguration();
            boolean needUpdate = false;
            for (String key : configuration.getKeys(false)) {
                try {
                    final Loc3D loc3d = BlockApi.strToLoc3d(key);
                    final String loc3s = BlockApi.locToStr(loc3d);
                    current.set(loc3s, configuration.getString(key));
                    needUpdate |= !loc3s.equals(key);
                } catch (Throwable any) {
                    logger.log(Level.WARNING, "[DataConverter] [" + name + "] Invalid loc3d `" + key + "`");
                }
            }
            if (needUpdate) {
                try {
                    current.save(blocks);
                } catch (IOException ioException) {
                    logger.log(Level.SEVERE, "[DataConverter] [" + name + "] Exception in updating loc3d", ioException);
                }
            }
        }
    }
}
