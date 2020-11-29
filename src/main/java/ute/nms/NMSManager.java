package ute.nms;

import org.bukkit.Bukkit;

public class NMSManager {
    public static String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    public static Class<?> getClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
            } catch (ClassNotFoundException e1) {
                return null;
            }
        }
    }
}
