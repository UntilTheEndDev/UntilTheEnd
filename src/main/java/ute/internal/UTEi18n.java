/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/09 22:27:45
 *
 * UntilTheEnd/UntilTheEnd/UTEi18n.java
 */

package ute.internal;

import ute.Config;
import ute.Logging;
import ute.UntilTheEnd;
import HamsterYDS.UntilTheEnd.internal.karlatemp.mxlib.formatter.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import ute.internal.karlatemp.mxlib.formatter.*;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.logging.Level;

public class UTEi18n {
    public static final Map<String, FormatTemplate> templates = new HashMap<>();
    public static final Map<String, String> cache = new ConcurrentHashMap<>();
    private static final Function<String, String> cache_loader = k -> parse(k);
    private static final Function<String, String> cache_loader2 = k -> cache("prefix") + parse(k);

    static {
        PunctuateFormatter pf = new PunctuateFormatter("{", "}");
        final YamlConfiguration configuration;
        {
            YamlConfiguration temp;
            String selected = UntilTheEnd.getInstance().getConfig().getString("language", "i18n.zh_cn.yml");
            Logging.getLogger().log(Level.INFO, "loading i18n with [" + selected + "]");
            try {
                temp = Config.autoUpdateConfigs(selected);
            } catch (Throwable exception) {
                Logging.getLogger().log(Level.WARNING, "Failed to load i18n configuration[" + selected + "], try to load it from default language [i18n.zh_cn.yml].", exception);
                try {
                    temp = Config.autoUpdateConfigs("i18n.zh_cn.yml");
                } catch (Throwable throwable) {
                    Logging.getLogger().log(Level.SEVERE, "Failed to load default configuration. Try to override it and reload!");
                    try {
                        UntilTheEnd.getInstance().saveResource("i18n.zh_cn.yml", true);
                        temp = YamlConfiguration.loadConfiguration(new File(UntilTheEnd.getInstance().getDataFolder(), "i18n.zh_cn.yml"));
                    } catch (Throwable step) {
                        Logging.getLogger().log(Level.SEVERE, "FAILED TO LOAD I18N FROM PLUGIN RESOURCE. WAS IT EDITED? NO LANGUAGE SUPPORT!");
                        temp = new YamlConfiguration();
                    }
                }
            }
            configuration = temp;
        }
        for (Map.Entry<String, Object> val : configuration.getValues(true).entrySet()) {
            final Object value = val.getValue();
            if (value instanceof ConfigurationSection) continue;
            String key = val.getKey();
            if (key.isEmpty()) continue;
            if (key.charAt(0) == '.') key = key.substring(1);
            final FormatTemplate parse = pf.parse(String.valueOf(val.getValue()), Replacer.EMPTY);
            FormatAction.ActionLink link = (FormatAction.ActionLink) (((SimpleFormatTemplate) parse)).getAction();
            ConcurrentLinkedQueue<FormatAction> actions = new ConcurrentLinkedQueue<>(link.getActions());
            link.clear();
            for (FormatAction action : actions) {
                if (action instanceof FormatAction.ActionGetKey) {
                    final String s = ((FormatAction.ActionGetKey) action).getKey();
                    try {
                        link.appendSlot(Integer.parseInt(s));
                        continue;
                    } catch (Throwable ignore) {
                        link.append("{" + s + "}");
                    }
                }
                link.addThen(action);
            }
            templates.put(key, parse);
        }
    }

    public static String parse(String key, String... args) {
        final FormatTemplate template = templates.get(key);
        if (template != null) {
            return template.format(Locale.ROOT, new ArrayReplacer(args));
        }
        return key;
    }

    public static String cacheWithPrefix(String key) {
        return cache.computeIfAbsent(key, cache_loader2);
    }

    public static String cache(String key) {
        return cache.computeIfAbsent(key, cache_loader);
    }
}
