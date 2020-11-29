package ute.nms.v1_13_R2;

import ute.nms.ActionBarManager;
import net.minecraft.server.v1_13_R2.ChatComponentText;
import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBarManagerImpl extends ActionBarManager {
    @Override
    protected void sendActionBar0(Player player, String line) {
        EntityPlayer player1 = ((CraftPlayer) player).getHandle();
        player1.playerConnection.sendPacket(new PacketPlayOutChat(new ChatComponentText(line), ChatMessageType.GAME_INFO));
    }
}
