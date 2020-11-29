package ute.item.survival;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import ute.cap.tem.TemperatureProvider;
import ute.item.ItemManager;

import java.util.List;

public class WarmStone implements Listener {
    public WarmStone() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onThrow(PlayerDropItemEvent event) {
        ItemStack ritem = event.getItemDrop().getItemStack();
        ItemStack item = event.getItemDrop().getItemStack();
        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().hasDisplayName()) return;
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.items.get("WarmStone").displayName)) {
            ItemMeta meta = item.getItemMeta();
            List<String> lores = meta.getLore();
            for (String str : lores) {
                if (str.contains("§8- §8§l温度 ")) {
                    lores.remove(str);
                    break;
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    lores.add("§8- §8§l温度 " + (int) TemperatureProvider.getBlockTemperature(event.getItemDrop().getLocation()));
                    meta.setLore(lores);
                    ritem.setItemMeta(meta);
                    event.getItemDrop().setItemStack(ritem);
                }
            }.runTaskLater(ItemManager.plugin, 20);
        }
    }

    public static boolean hasTemperature(ItemStack item) {
        if (!item.hasItemMeta()) return false;
        if (!item.getItemMeta().hasLore()) return false;
        ItemMeta meta = item.getItemMeta();
        List<String> lores = meta.getLore();
        for (String str : lores)
            if (str.contains("§8- §8§l温度 "))
                if (!str.replace("§8- §8§l温度 ", "").equalsIgnoreCase(""))
                    return true;
        return false;
    }
}
