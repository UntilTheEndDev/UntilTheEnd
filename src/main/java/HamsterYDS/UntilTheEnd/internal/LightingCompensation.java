/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/09 21:33:30
 *
 * UntilTheEnd/UntilTheEnd/LightingCompensation.java
 */

package HamsterYDS.UntilTheEnd.internal;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LightingCompensation {
    public static final Map<UUID, Integer> comp = new HashMap<>();
    public static final Map<String, Integer> config = new HashMap<>();
    private static boolean initialize;

    public static int getComp(UUID world) {
        final Integer integer = comp.get(world);
        if (integer == null) return 0;
        return integer;
    }

    private static void update(World world) {
        final Integer integer = config.get(world.getName().toLowerCase());
        if (integer == null) return;
        comp.put(world.getUID(), integer);
    }

    public static void reload() {
        UntilTheEnd pl = UntilTheEnd.getInstance();
        comp.clear();
        config.clear();
        pl.saveResource("LightingCompensation.yml", false);
        final YamlConfiguration setting = YamlConfiguration.loadConfiguration(new File(pl.getDataFolder(), "LightingCompensation.yml"));
        for (String key : setting.getKeys(false)) {
            config.put(key.toLowerCase(), setting.getInt(key));
        }
        Bukkit.getWorlds().forEach(LightingCompensation::update);
        if (initialize) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler()
            void onWorldLoad(WorldLoadEvent event) {
                update(event.getWorld());
            }
        }, pl);
        initialize = true;
    }

    static {
        reload();
    }
}
