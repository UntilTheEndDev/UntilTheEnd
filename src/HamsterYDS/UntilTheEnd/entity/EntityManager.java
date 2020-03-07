package HamsterYDS.UntilTheEnd.entity;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.entity.neutral.PigMan;
import net.minecraft.server.v1_12_R1.WorldServer;

public class EntityManager {
	public static UntilTheEnd plugin;
	public static WorldServer world=((CraftWorld)Bukkit.getServer().getWorld("world")).getHandle();
	public EntityManager(UntilTheEnd plugin) {
		this.plugin=plugin;
		for(Player player:Bukkit.getOnlinePlayers()) {
			PigMan.spawnPigMan(player.getLocation());
		}
	}
}
