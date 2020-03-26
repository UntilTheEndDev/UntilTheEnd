package HamsterYDS.UntilTheEnd.cap.san;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import me.clip.placeholderapi.PlaceholderAPI;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class InfluenceEvents implements Listener {
    public static double moveWronglySanity = Sanity.yaml.getDouble("moveWronglySanity");
    public static double moveWronglyPercent = Sanity.yaml.getDouble("moveWronglyPercent");
    public static double chatablessSanity = Sanity.yaml.getDouble("chatablessSanity");

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        int san = (int) PlayerManager.check(player, PlayerManager.CheckType.SANITY);
        int moveWronglySanityCal = (int) (moveWronglySanity * PlayerManager.check(player, PlayerManager.CheckType.SANMAX));
        if (san <= moveWronglySanityCal && Math.random() <= moveWronglyPercent) {
            Location loc = event.getTo();
            float fac = (float) ((moveWronglySanityCal - san) / 3F);
            if (fac < 0) return;
            loc.setYaw((float) (loc.getYaw() + Math.random() * fac - Math.random() * fac));
            loc.setPitch((float) (loc.getPitch() + Math.random() * fac - Math.random() * fac));
            event.setTo(loc);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        String newString = "";
        Player sender = event.getPlayer();
        event.setMessage(PlaceholderAPI.setPlaceholders(sender, event.getMessage()));
        if (!Config.enableWorlds.contains(sender.getWorld())) return;
        int san = (int) PlayerManager.check(sender, PlayerManager.CheckType.SANITY);
        int chatablessSanityCal = (int) (chatablessSanity * PlayerManager.check(sender, PlayerManager.CheckType.SANMAX));
        if (san <= chatablessSanityCal) {
            for (int i = 0; i < Math.random() * 50; i++)
                newString += (char) (10000 * Math.random() + 40);
            event.setMessage(newString);
        }
    }
}
