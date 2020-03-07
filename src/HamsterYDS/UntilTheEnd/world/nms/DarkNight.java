package HamsterYDS.UntilTheEnd.world.nms;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.PluginApi;

public class DarkNight extends BukkitRunnable{
	@Override
	public void run() {
		for(World world:Config.enableWorlds){
			if(world.getTime()>=HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getLong("world.blind.up")
			&&world.getTime()<=HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getLong("world.blind.down")) {
				for(Player player:world.getPlayers()) {
					try {
						sendDarkPacket(player);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException | NoSuchFieldException
							| InstantiationException e) {
						e.printStackTrace();
					}
				}
			}else {
				for(Player player:world.getPlayers()) {
					try {
						sendDayPacket(player);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException | NoSuchFieldException
							| InstantiationException e) {
						e.printStackTrace();
					}
				}
			} 
		}
	}
	public void sendDarkPacket(Player player) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, InstantiationException {
		Object entityPlayer=player.getClass().getMethod("getHandle").invoke(player);
		Object playerConnection=entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
		Object packet=PluginApi.getNMS("PacketPlayOutGameStateChange")
				.getConstructor(int.class,float.class)
				.newInstance(8,0.0f);
		playerConnection.getClass().getMethod("sendPacket",PluginApi.getNMS("Packet")).invoke(playerConnection, packet);
	}
	public void sendDayPacket(Player player) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, InstantiationException {
		Object entityPlayer=player.getClass().getMethod("getHandle").invoke(player);
		Object playerConnection=entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
		Object packet=PluginApi.getNMS("PacketPlayOutGameStateChange")
				.getConstructor(int.class,float.class)
				.newInstance(7,0.0f);
		playerConnection.getClass().getMethod("sendPacket",PluginApi.getNMS("Packet")).invoke(playerConnection, packet);
	}
}

