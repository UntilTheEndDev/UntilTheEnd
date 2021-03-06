package ute.nms.v1_16_R4;

import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import ute.nms.ActionBarManager;

import java.util.UUID;

public class ActionBarManagerImpl extends ActionBarManager {
    private static final UUID ZERO = new UUID(0, 0);

    @Override
    protected void sendActionBar0(Player player, String line) {
        EntityPlayer player1 = ((CraftPlayer) player).getHandle();
        player1.playerConnection.sendPacket(new PacketPlayOutChat(new ChatComponentText(line), ChatMessageType.GAME_INFO, ZERO));
    }
}
