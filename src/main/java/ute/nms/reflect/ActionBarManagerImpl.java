package ute.nms.reflect;

import ute.UntilTheEnd;
import ute.nms.ActionBarManager;
import ute.nms.NMSHelper;
import ute.nms.ReflectionUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class ActionBarManagerImpl extends ActionBarManager {
    static final ReflectionUtil.UncheckedCode<Object> packetAllocator;

    // EntityPlayer player1 = ((CraftPlayer) player).getHandle();
    // player1.playerConnection.sendPacket(new PacketPlayOutChat(new ChatComponentText(line), ChatMessageType.GAME_INFO));
    static {
        Class<?> PacketPlayOutChat = ReflectionUtil.getNMSClass("PacketPlayOutChat");
        Class<?> ChatMessageType = ReflectionUtil.getNMSClass("ChatMessageType");
        Class<?> ChatComponentText = ReflectionUtil.getNMSClass("ChatComponentText");
        Constructor<?> ChatComponentText$new = ReflectionUtil.getConstructor(ChatComponentText, String.class);
        assert ChatComponentText$new != null;
        ChatComponentText$new.setAccessible(false);

        Class<?> IChatBaseComponent = ReflectionUtil.getNMSClass("IChatBaseComponent");
        ReflectionUtil.UncheckedCode<Object> newComponentText = ChatComponentText$new::newInstance;
        if (ChatMessageType == null) {
            // 1.12.2 -
            Constructor<?> PacketPlayOutChat$CCT_byte = ReflectionUtil.getConstructor(PacketPlayOutChat, IChatBaseComponent, byte.class);
            assert PacketPlayOutChat$CCT_byte != null;
            PacketPlayOutChat$CCT_byte.setAccessible(false);
            Byte b = (byte) 2;
            packetAllocator = args -> PacketPlayOutChat$CCT_byte.newInstance(newComponentText.call(args), b);
        } else {
            // 1.12.2 +
            Constructor<?> PacketPlayOutChat$CCT_Type = ReflectionUtil.getConstructor(PacketPlayOutChat, IChatBaseComponent, ChatMessageType);
            assert PacketPlayOutChat$CCT_Type != null;
            PacketPlayOutChat$CCT_Type.setAccessible(false);
            // ChatMessageType.GAME_INFO
            Object GAME_INFO = ReflectionUtil.runUncheck(args -> ChatMessageType.getField("GAME_INFO").get(null));
            packetAllocator = args -> PacketPlayOutChat$CCT_Type.newInstance(newComponentText.call(args), GAME_INFO);
        }

    }

    @Override
    protected void sendActionBar0(Player player, String line) {
        if(!UntilTheEnd.getInstance().getConfig().getBoolean("actionbartips")){
            return;
        }
        NMSHelper.sendPacket(
                NMSHelper.getPlayerConnection(
                        NMSHelper.getHandle(player)
                ),
                ReflectionUtil.runUncheck(packetAllocator, line)
        );
    }
}
