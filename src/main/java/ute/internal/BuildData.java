/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/07/07 13:21:33
 *
 * until-the-end/until-the-end.main/BuildData.java
 */

package ute.internal;

import ute.Logging;
import ute.UntilTheEnd;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;

public class BuildData {
    public static final String GIT_COMMIT;
    public static final long BUILD_TIME;
    public static final boolean BUILD_BY_GITHUB;
    public static final String BUILDER;

    static {
        String builder = "UNKNOWN";
        String gitCommit = "UNKNOWN";
        long buildTime = -1L;
        final InputStream resource = UntilTheEnd.getInstance().getResource("META-INF/MANIFEST.MF");
        if (resource == null) {
            Logging.getLogger().warning("MANIFEST Missing");
        } else {
            Manifest manifest = new Manifest();
            try (InputStream stream = resource) {
                manifest.read(stream);
            } catch (IOException exception) {
                Logging.getLogger().log(Level.WARNING, "Exception in reading manifest.");
            }
            final Attributes attributes = manifest.getMainAttributes();
            gitCommit = attributes.getValue("Implementation-Version");
            try {
                buildTime = Long.parseLong(attributes.getValue("BuildTimestamp"));
            } catch (Throwable ignore) {
            }
            if (!UntilTheEnd.getInstance().getDescription().getVersion().equals(attributes.getValue("Application-Version"))) {
                Logging.getLogger().log(Level.WARNING, "Version not match! The jar of UntilTheEnd may be damaged.");
            }
            builder = attributes.getValue("Created-By");
        }
        GIT_COMMIT = gitCommit;
        BUILD_TIME = buildTime;
        BUILDER = builder;
        BUILD_BY_GITHUB = "Github Action".equalsIgnoreCase(builder);
        if (!BUILD_BY_GITHUB) {
            Logging.getLogger().warning("You are using custom build.");
        }
    }

    public static void checkSystem() {
        ItemStack itemStack = new ItemStack(Material.AIR);
        itemStack.setType(Material.STAINED_GLASS_PANE);
        if (itemStack.getItemMeta() == null) {
            throw new AssertionError(
                    "ERROR: UntilTheEnd Not WORK on This Server version. Please Update Your Server CORE!",
                    removeThreadStack(
                            new AssertionError("" +
                                    "Bukkit.getVersion()      : " + Bukkit.getVersion() + "\n" +
                                    "Bukkit.getBukkitVersion(): " + Bukkit.getBukkitVersion() + "\n" +
                                    "Java: " + System.getProperty("java.home") + "\n\n" +
                                    "@see SPIGOT-5536: https://hub.spigotmc.org/jira/browse/SPIGOT-5536"
                            )
                    )
            );
        }
    }

    public static Throwable removeThreadStack(Throwable throwable) {
        throwable.setStackTrace(new StackTraceElement[0]);
        return throwable;
    }
}
