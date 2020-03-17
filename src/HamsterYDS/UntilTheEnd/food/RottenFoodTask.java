package HamsterYDS.UntilTheEnd.food;

import java.util.ArrayList;
import java.util.List;

import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

public class RottenFoodTask {
    private static ItemStack rottenFood = new ItemStack(Material.ROTTEN_FLESH);
    public static UntilTheEnd plugin;

    public RottenFoodTask(UntilTheEnd plugin) {
        this.plugin = plugin;
        ItemMeta meta = rottenFood.getItemMeta();
        meta.setDisplayName("§6腐烂食物");
        rottenFood.setItemMeta(meta);
        new RottenFood().runTaskTimer(plugin, 0L, plugin.getConfig().getInt("food.rotten.invspeed") * 20);
    }

    public class RottenFood extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)) continue;
                    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
                        continue;
                    PlayerInventory inv = player.getInventory();
                    if (getName(inv.getChestplate()).equalsIgnoreCase("§6便携包")) continue;
                    for (int slot = 0; slot < inv.getSize(); slot++) {
                        ItemStack item = inv.getItem(slot);
                        if (item == null) continue;
                        if (item.getType() == Material.ROTTEN_FLESH) continue;
                        if (item.getType().isEdible())
                            inv.setItem(slot, setRottenLevel(item, getRottenLevel(item) - 1));
                    }
                }
        }
    }

    public String getName(ItemStack item) {
        if (item == null) return "";
        if (item.hasItemMeta())
            if (item.getItemMeta().hasDisplayName())
                return item.getItemMeta().getDisplayName();
        return "";
    }

    public static int getRottenLevel(ItemStack item) {
        if (item == null) return 100;
        if (item.hasItemMeta())
            if (item.getItemMeta().hasLore())
                for (String str : item.getItemMeta().getLore())
                    if (str.contains("§8- §8§l新鲜度 "))
                        return Integer.valueOf(str.replace("§8- §8§l新鲜度 ", ""));
        return 100;
    }

    public static ItemStack setRottenLevel(ItemStack item, int currentLevel) {
        if (item == null) return item;
        if (currentLevel <= 0) {
            ItemStack rotten = rottenFood.clone();
            rotten.setAmount(item.getAmount());
            return rotten;
        }
        ItemMeta meta = item.getItemMeta();
        List<String> lores = new ArrayList<String>();
        if (meta != null) {
            if (meta.hasLore()) {
                lores = meta.getLore();
                if (hasTag(lores)) return item;
                for (String str : item.getItemMeta().getLore())
                    if (str.contains("§8- §8§l新鲜度 ")) {
                        lores.remove(str);
                        str = "§8- §8§l新鲜度 " + currentLevel;
                        lores.add(str);
                        break;
                    }
            } else lores.add("§8- §8§l新鲜度 " + currentLevel);
        } else lores.add("§8- §8§l新鲜度 " + currentLevel);
        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;
    }

    private static boolean hasTag(List<String> lores) {
        for (String str : lores)
            if (str.contains("不可腐烂"))
                return true;
        return false;
    }
}
