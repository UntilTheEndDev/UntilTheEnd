package HamsterYDS.UntilTheEnd.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ActionBarManager {
	static Class<?> ChatComponentText;
	static Constructor<?> makeChatComponentText;
	static Class<?> IChatBaseComponent;
	static Class<?> ClassChatMessageType;
	static Class<?> PacketPlayOutChat;
	static Constructor<?> makePacketPlayOutChat;
	static Class<?> CraftPlayer;
	static Class<?> EntityPlayer;
	static Class<?> PlayerConnection;
	static Class<?> Packet;
	static Method sendPacket;
	static Method makeChatType;
	static Method getHandle;
	public Class<?> getClass(String name) {
		try {
			return Class.forName("net.minecraft.server."+NMSManager.version+"."+name);
		} catch (ClassNotFoundException e) {
			try {
				return Class.forName("org.bukkit.craftbukkit."+NMSManager.version+"."+name);
			} catch (ClassNotFoundException e1) {
				return null;
			}
		}
	}
}
