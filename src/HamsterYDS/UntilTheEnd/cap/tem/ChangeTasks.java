package HamsterYDS.UntilTheEnd.cap.tem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

public class ChangeTasks {
    public static UntilTheEnd plugin;
    public static long temperatureChangeSpeedStone = Temperature.yaml.getLong("temperatureChangeSpeedStone");
    public static long temperatureChangeSpeedNatural = Temperature.yaml.getLong("temperatureChangeSpeedNatural");
    public static double stoneChangePercent = Temperature.yaml.getDouble("stoneChangePercent");
    public static long humidityChangeSpeed = Temperature.yaml.getLong("humidityChangeSpeed");
    public static HashMap<String, Double> clothesChangeTemperature = new HashMap<String, Double>();

    public ChangeTasks(UntilTheEnd plugin) {
        this.plugin = plugin;
        new PlayerTask().runTaskTimer(plugin, 0L, 20L);
        new HumidityTask().runTaskTimer(plugin, 0L, humidityChangeSpeed);
    }

    public class HumidityTask extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    int hum = PlayerManager.check(player, PlayerManager.CheckType.HUMIDITY);
                    PlayerManager.change(player, PlayerManager.CheckType.TEMPERATURE, -hum / 5);
                }
        }
    }

    public class PlayerTask extends BukkitRunnable {
        public boolean goWarmStone(Player player) {
            PlayerInventory inv = player.getInventory();
            for (int slot = 0; slot < inv.getSize(); slot++) {
                ItemStack item = inv.getItem(slot);
                if (item == null) continue;
                ItemMeta meta = item.getItemMeta();
                if (meta == null) continue;
                if (meta.getDisplayName() == null) continue;
                if (meta.getDisplayName().equalsIgnoreCase("§6暖石")) {
                    List<String> lores = meta.getLore();
                    for (String line : lores) {
                        if (!line.contains("§8- §8§l温度 ")) continue;
                        line = line.replace("§8- §8§l温度 ", "");
                        int naturalTem = TemperatureProvider.getBlockTemperature(player.getLocation());
                        int stoneTem = Integer.parseInt(line);
                        int playerTem = PlayerManager.check(player, PlayerManager.CheckType.TEMPERATURE);
                        if (playerTem < stoneTem) PlayerManager.change(player, PlayerManager.CheckType.TEMPERATURE, 1);
                        if (playerTem > stoneTem) PlayerManager.change(player, PlayerManager.CheckType.TEMPERATURE, -1);
                        if (Math.random() <= stoneChangePercent) {
                            if (stoneTem > naturalTem)
                                line = "§8- §8§l温度 " + (stoneTem - 1);
                            if (stoneTem < naturalTem)
                                line = "§8- §8§l温度 " + (stoneTem + 1);
                            if (stoneTem == naturalTem)
                                line = "§8- §8§l温度 " + (stoneTem);
                            lores.remove(lores.size() - 1);
                            lores.add(line);
                            meta.setLore(lores);
                            item.setItemMeta(meta);
                            inv.setItem(slot, item);
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public void goNatural(Player player) {
            final int naturalTem = TemperatureProvider.getBlockTemperature(player.getLocation());
            final int playerTem = PlayerManager.check(player, PlayerManager.CheckType.TEMPERATURE);
            if (playerTem < naturalTem && clothesChange(player, true))
                PlayerManager.change(player, PlayerManager.CheckType.TEMPERATURE, 1);
            if (playerTem > naturalTem && clothesChange(player, false))
                PlayerManager.change(player, PlayerManager.CheckType.TEMPERATURE, -1);
        }

        public boolean clothesChange(Player player, boolean upOrDown) {
            double upFactor = 0.0;
            double downFactor = 0.0;
            PlayerInventory inv = player.getInventory();
            for (ItemStack item : inv.getArmorContents())
                if (clothesChangeTemperature.containsKey(getName(item))){
                    if (clothesChangeTemperature.get(getName(item)) > 0) {
                        upFactor += clothesChangeTemperature.get(getName(item));
                    } else downFactor += clothesChangeTemperature.get(getName(item));
                }
            if(upFactor==2) return false;
            if (upOrDown) {
				return !(Math.random() < upFactor);
            } else {
				return !(Math.random() < downFactor);
            }
		}

        public String getName(ItemStack item) {
            if (item != null)
                if (item.hasItemMeta())
                    if (item.getItemMeta().hasDisplayName())
                        return item.getItemMeta().getDisplayName();
            return "";
        }

        public ArrayList<String> hasStone = new ArrayList<String>();
        public long totStone = 0;
        public long totNatural = 0;

        @Override
        public void run() {
            totStone++;
            totNatural++;
            if (totStone % temperatureChangeSpeedStone == 0) {
                totStone = 0;
                hasStone = new ArrayList<String>();
                for (World world : Config.enableWorlds)
                    for (Player player : world.getPlayers())
                        if (goWarmStone(player))
                            hasStone.add(player.getName());
            }
            if (totNatural % temperatureChangeSpeedNatural == 0) {
                totNatural = 0;
                for (World world : Config.enableWorlds)
                    for (Player player : world.getPlayers())
                        if (hasStone.contains(player.getName())) continue;
                        else goNatural(player);
            }
        }
    }
}