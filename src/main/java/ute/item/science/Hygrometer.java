package ute.item.science;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import ute.Config;
import ute.api.BlockApi;
import ute.event.block.CustomBlockInteractEvent;
import ute.internal.UTEi18n;
import ute.item.ItemManager;

import java.util.ArrayList;

public class Hygrometer implements Listener {
    public static int existPeriod = ItemManager.itemAttributes.getInt("Hygrometer.existPeriod");

    public Hygrometer() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    ArrayList<String> clicked = new ArrayList<String>();

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onClick(CustomBlockInteractEvent event) {
        Player player = event.getWho();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        Block block = event.getClickedBlock();
        Location loc = block.getLocation();
        String toString = BlockApi.locToStr(loc);
        if (event.getCustomItem().id.equalsIgnoreCase("Hygrometer")) {
            if (clicked.contains(toString)) return;
            clicked.add(toString);
            String text = UTEi18n.parse("item.hygrometer.main.main", (loc.getWorld().hasStorm()
                    ? UTEi18n.cache("item.hygrometer.main.rain") : UTEi18n.cache("item.hygrometer.main.sun"))
            );
            String text2 = UTEi18n.parse("item.hygrometer.sub.text", String.valueOf(loc.getWorld().getWeatherDuration() / 20));
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
