/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/09 22:27:45
 *
 * UntilTheEnd/UntilTheEnd/UTEi18n.java
 */

package HamsterYDS.UntilTheEnd.internal;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.internal.karlatemp.mxlib.formatter.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

public class UTEi18n {
    public static final Map<String, FormatTemplate> templates = new HashMap<>();
    public static final Map<String, String> cache = new ConcurrentHashMap<>();
    private static final Function<String, String> cache_loader = k -> parse(k);
    private static final Function<String, String> cache_loader2 = k -> cache("prefix") + parse(k);

    static {
        PunctuateFormatter pf = new PunctuateFormatter("{", "}");
        final YamlConfiguration configuration = Config.autoUpdateConfigs(UntilTheEnd.getInstance().getConfig().getString("language"));
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
