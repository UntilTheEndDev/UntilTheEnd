/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/12 18:11:38
 *
 * UntilTheEnd/UntilTheEnd/DisableManager.java
 */

package HamsterYDS.UntilTheEnd.internal;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.Logging;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class DisableManager {
    public static final YamlConfiguration root = Config.autoUpdateConfigs("disable.yml");
    public static boolean bypass_right_action_cancelled;

    static {
        Logging.getLogger().fine("[DisableManager] Loading disable manager's data....");
        bypass_right_action_cancelled = root.getBoolean("bypass_right_action_cancelled", false);
    }

    public static class CheckTypeChecking {
        private static final ConfigurationSection section = root.getConfigurationSection("check-type");
        private static final Map<String, EnumSet<PlayerManager.CheckType>> mapping = new HashMap<>();
        private static final EnumSet<PlayerManager.CheckType> def;
        private static final Map<UUID, EnumSet<PlayerManager.CheckType>> cache = new HashMap<>();

        public static EnumSet<PlayerManager.CheckType> getDisabled(World world) {
            EnumSet<PlayerManager.CheckType> checkTypes = cache.get(world.getUID());
            if (checkTypes == null) {
                checkTypes = mapping.getOrDefault(world.getName().toLowerCase(), def);
                cache.put(world.getUID(), checkTypes);
            }
            return checkTypes;
        }

        public static EnumSet<PlayerManager.CheckType> parse(Collection<String> list) {
            EnumSet<PlayerManager.CheckType> es = EnumSet.noneOf(PlayerManager.CheckType.class);
            if (list == null) return es;
            for (String opt : list) {
                final PlayerManager.CheckType search = PlayerManager.CheckType.search(opt);
                if (search != null)
                    es.add(search);
            }
            return es;
        }

        static {
            for (String wn : section.getKeys(false)) {
                ConfigurationSection val = section.getConfigurationSection(wn);
                final EnumSet<PlayerManager.CheckType> list = parse(val.getStringList("list"));
                mapping.put(wn.toLowerCase(), list);
                for (String mirror : val.getStringList("mirror")) {
                    mapping.put(mirror.toLowerCase(), list);
                }
            }
            def = mapping.getOrDefault("$default", EnumSet.noneOf(PlayerManager.CheckType.class));
        }
    }
}
