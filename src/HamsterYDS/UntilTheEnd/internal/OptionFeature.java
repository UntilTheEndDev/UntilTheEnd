/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/11 17:34:04
 *
 * UntilTheEnd/UntilTheEnd/OptionFeature.java
 */

package HamsterYDS.UntilTheEnd.internal;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class OptionFeature {
    public static Collection<String> disabled = new HashSet<>();
    public static Collection<String> all = new ConcurrentLinkedQueue<>();

    static {
        File list = new File(UntilTheEnd.getInstance().getDataFolder(), "disabled.txt");
        if (!list.isFile()) {
            try {
                list.getParentFile().mkdirs();
                list.createNewFile();
                Files.write(list.toPath(), ("" +
                        "# This is a list of disabled UTE features\n" +
                        "# If you want to dump all features. Use \"/ute dump-features\" to dump them.\n" +
                        "").getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                UntilTheEnd.getInstance().getLogger().log(Level.SEVERE, "Failed to save disabled list.", e);
            }
        } else {
            try {
                try (FileInputStream fis = new FileInputStream(list)) {
                    try (InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
                        try (BufferedReader buffer = new BufferedReader(reader)) {
                            String line;
                            while ((line = buffer.readLine()) != null) {
                                line = line.trim();
                                if (line.isEmpty()) continue;
                                if (line.charAt(0) == '#') continue;
                                disabled.add(line);
                            }
                        }
                    }
                }
            } catch (Throwable any) {
                UntilTheEnd.getInstance().getLogger().log(Level.SEVERE, "Exception in loading disabled list.", any);
            }
        }
    }

    public String getFeatureName() {
        return getClass().getName();
    }

    public boolean isEnable() {
        return !disabled.contains(getFeatureName());
    }

    protected abstract void boot();

    public void start() {
        String uniqueId = getFeatureName();
        if (!all.contains(uniqueId)) all.add(uniqueId);
        if (isEnable()) boot();
        else UntilTheEnd.getInstance().getLogger().log(Level.WARNING, "Feature " + uniqueId + " disabled.");
    }
}
