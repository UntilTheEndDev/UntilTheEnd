package HamsterYDS.UntilTheEnd.nms;

import org.bukkit.Bukkit;

public class NMSManager {
	public static String version=Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
}
