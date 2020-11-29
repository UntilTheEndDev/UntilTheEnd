package ute.cap.san;

import ute.api.PlayerApi;
import ute.internal.DisableManager;
import ute.internal.SanChattingProvider;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import ute.Config;
import ute.player.PlayerManager;

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
            float fac = (moveWronglySanityCal - san) / 3F;
            if (fac < 0) return;
            loc.setYaw((float) (loc.getYaw() + Math.random() * fac - Math.random() * fac));
            loc.setPitch((float) (loc.getPitch() + Math.random() * fac - Math.random() * fac));
            event.setTo(loc);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        if (!DisableManager.root.getBoolean("chatting_placeholder_api", true))
            event.setMessage(PlayerApi.getPAPI(sender, event.getMessage()));
        if (!Config.enableWorlds.contains(sender.getWorld())) return;
        int san = (int) PlayerManager.check(sender, PlayerManager.CheckType.SANITY);
        int chatablessSanityCal = (int) (chatablessSanity * PlayerManager.check(sender, PlayerManager.CheckType.SANMAX));
        if (san <= chatablessSanityCal) {
            event.setMessage(SanChattingProvider.INSTANCE.apply(event.getMessage()));
        }
    }
}
