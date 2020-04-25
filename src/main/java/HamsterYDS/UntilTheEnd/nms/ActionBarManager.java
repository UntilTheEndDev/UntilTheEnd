package HamsterYDS.UntilTheEnd.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import HamsterYDS.UntilTheEnd.Logging;
import org.bukkit.entity.Player;

public class ActionBarManager {
    static Class<?> ChatComponentText = NMSManager.getClass("ChatComponentText");
    static Constructor<?> makeChatComponentText;
    static Class<?> IChatBaseComponent = NMSManager.getClass("IChatBaseComponent");
    static Class<?> ClassChatMessageType;
    static Class<?> PacketPlayOutChat = NMSManager.getClass("PacketPlayOutChat");
    static Constructor<?> makePacketPlayOutChat;
    static Class<?> CraftPlayer = NMSManager.getClass("entity.CraftPlayer");
    static Class<?> EntityPlayer = NMSManager.getClass("EntityPlayer");
    static Class<?> PlayerConnection = NMSManager.getClass("PlayerConnection");
    static Class<?> Packet = NMSManager.getClass("Packet");
    static Method sendPacket;
    static Method makeChatType;
    static Method getHandle;

    public ActionBarManager() throws NoSuchMethodException, SecurityException {
        sendPacket = PlayerConnection.getMethod("sendPacket", Packet);
        getHandle = CraftPlayer.getMethod("getHandle");
        makeChatComponentText = ChatComponentText.getConstructor(String.class);
        if (NMSManager.version.equals("v1_12_R1")) {
            ClassChatMessageType = NMSManager.getClass("ChatMessageType");
            makeChatType = ClassChatMessageType.getMethod("a", Byte.TYPE);
            makePacketPlayOutChat = PacketPlayOutChat.getConstructor(IChatBaseComponent, ClassChatMessageType);
        } else {
            makePacketPlayOutChat = PacketPlayOutChat.getConstructor(IChatBaseComponent, Byte.class);
        }
    }

    public static void sendActionBar(Player player, String line) {
        Object obj;
        try {
            obj = makeChatComponentText.newInstance(line);
            Object bar;
            if (NMSManager.version.equals("v1_12_R1"))
                bar = makePacketPlayOutChat.newInstance(obj, makeChatType.invoke(null, (byte) 2));
            else bar = makePacketPlayOutChat.newInstance(obj, (byte) 2);
            Object entityPlayer = getHandle.invoke(CraftPlayer.cast(player));
            Field con = entityPlayer.getClass().getDeclaredField("playerConnection");
            Object objCon = con.get(entityPlayer);
            sendPacket.invoke(objCon, bar);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchFieldException | SecurityException e) {
            Logging.getLogger().log(Level.SEVERE, "Failed to send packet to " + player.getName(), e);
        }
    }
}
