package HamsterYDS.UntilTheEnd.item.science;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.api.BlockApi;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Hygrometer implements Listener {
    public static int existPeriod = ItemManager.itemAttributes.getInt("Hygrometer.existPeriod");

    public Hygrometer() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    ArrayList<String> clicked = new ArrayList<String>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Location loc = block.getLocation();
        String toString = BlockApi.locToStr(loc);
        if (BlockApi.getSpecialBlocks("Hygrometer").contains(toString)) {
            if (clicked.contains(toString)) return;
            clicked.add(toString);
            String text = "§e§l天气-§d§l" + (loc.getWorld().hasStorm() ? "雨雪" : "晴天");
            String text2 = "§e§l目前该天气还有§d§l" + (loc.getWorld().getWeatherDuration() / 20) + "§e§l秒";
            ArmorStand armor = (ArmorStand) player.getWorld().spawnEntity(loc.clone().add(0.5, 0.5, 0.5), EntityType.ARMOR_STAND);
            armor.setVisible(false);
            armor.setSmall(true);
            armor.setGravity(false);
            armor.setCustomName(text);
            armor.setCustomNameVisible(true);
            ArmorStand armor2 = (ArmorStand) player.getWorld().spawnEntity(loc.clone().add(0.5, 0.15, 0.5), EntityType.ARMOR_STAND);
            armor2.setVisible(false);
            armor2.setSmall(true);
            armor2.setGravity(false);
            armor2.setCustomName(text2);
            armor2.setCustomNameVisible(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    armor.remove();
                    armor2.remove();
                    clicked.remove(toString);
                    cancel();
                }
            }.runTaskTimer(ItemManager.plugin, existPeriod * 20, 1);
        }
    }
}
