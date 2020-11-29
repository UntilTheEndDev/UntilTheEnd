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
import ute.cap.tem.TemperatureProvider;
import ute.event.block.CustomBlockInteractEvent;
import ute.item.ItemManager;

import java.util.ArrayList;

public class Thermometer implements Listener {
    public static int existPeriod = ItemManager.itemAttributes.getInt("Thermometer.existPeriod");

    public Thermometer() {
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
        if (event.getCustomItem().id.equalsIgnoreCase("Thermometer")) {
            if (clicked.contains(toString)) return;
            clicked.add(toString);
            int tem = (int) TemperatureProvider.getBlockTemperature(loc);
            String text = "§e§l温度§d§l" + tem + "§e§l°C";
            ArmorStand armor = (ArmorStand) player.getWorld().spawnEntity(loc.clone().add(0.5, 0.5, 0.5), EntityType.ARMOR_STAND);
            armor.setVisible(false);
            armor.setSmall(true);
            armor.setGravity(false);
            armor.setCustomName(text);
            armor.setCustomNameVisible(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    armor.remove();
                    clicked.remove(toString);
                    cancel();
                }
            }.runTaskTimer(ItemManager.plugin, existPeriod * 20, 1);
        }
    }
}
