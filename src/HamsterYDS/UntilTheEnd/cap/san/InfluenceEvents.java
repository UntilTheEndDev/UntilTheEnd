package HamsterYDS.UntilTheEnd.cap.san;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class InfluenceEvents implements Listener {
    public static int moveWronglySanity = Sanity.yaml.getInt("moveWronglySanity");
    public static double moveWronglyPercent = Sanity.yaml.getDouble("moveWronglyPercent");
    public static int chatablessSanity = Sanity.yaml.getInt("chatablessSanity");

    public InfluenceEvents(UntilTheEnd plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        int san = PlayerManager.check(player, PlayerManager.CheckType.SANITY);
        if (san <= moveWronglySanity && Math.random() <= moveWronglyPercent) {
            Location loc = event.getTo();
            float fac = (moveWronglySanity - san) / 3F;
            if (fac < 0) return;
            loc.setYaw((float) (loc.getYaw() + Math.random() * fac - Math.random() * fac));
            loc.setPitch((float) (loc.getPitch() + Math.random() * fac - Math.random() * fac));
            event.setTo(loc);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        String newString = "";
        Player sender = event.getPlayer();
        if (!Config.enableWorlds.contains(sender.getWorld())) return;
        int san = PlayerManager.check(sender, PlayerManager.CheckType.SANITY);
        if (san <= chatablessSanity) {
            for (int i = 0; i < Math.random() * 50; i++)
                newString += (char) (10000 * Math.random() + 40);
            event.setMessage(newString);
        }
    }
}
