package ute.cap.tem;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import ute.Config;
import ute.UntilTheEnd;
import ute.api.PlayerApi;
import ute.event.cap.TemperatureChangeEvent;
import ute.internal.NPCChecker;
import ute.internal.ResidenceChecker;
import ute.item.ItemManager;
import ute.item.clothes.ClothesContainer;
import ute.item.survival.WarmStone;
import ute.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChangeTasks {
    public static UntilTheEnd plugin = UntilTheEnd.getInstance();
    public static long temperatureChangeSpeedStone = Temperature.yaml.getLong("temperatureChangeSpeedStone");
    public static long temperatureChangeSpeedNatural = Temperature.yaml.getLong("temperatureChangeSpeedNatural");
    public static double stoneChangePercent = Temperature.yaml.getDouble("stoneChangePercent");
    public static long humidityChangeSpeed = Temperature.yaml.getLong("humidityChangeSpeed");
    public static HashMap<String, Double> clothesChangeTemperature = new HashMap<String, Double>();

    public static void initialize(UntilTheEnd plugin) {
        new PlayerTask().runTaskTimer(plugin, 0L, 10L);
        new HumidityTask().runTaskTimer(plugin, 0L, humidityChangeSpeed);
    }

    public static class HumidityTask extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)|| ResidenceChecker.isProtected(player.getLocation())) continue;
                    double hum = PlayerManager.check(player, PlayerManager.CheckType.HUMIDITY);
                    PlayerApi.TemperatureOperations.changeTemperature(player, TemperatureChangeEvent.ChangeCause.HUMIDITY,-hum / 5);
                }
        }
    }

    public static class PlayerTask extends BukkitRunnable {
        public static boolean goWarmStone(Player player) {
            PlayerInventory inv = player.getInventory();
            for (int slot = 0; slot < inv.getSize(); slot++) {
                ItemStack item = inv.getItem(slot);
                if (item == null) continue;
                if (WarmStone.hasTemperature(item)) {
                    ItemMeta meta = item.getItemMeta();
                    List<String> lores = meta.getLore();
                    int line_line = -1;
                    for (String line : lores) {
                        line_line++;
                        if (!line.contains("§8- §8§l温度 ")) continue;
                        line = line.replace("§8- §8§l温度 ", "");
                        int naturalTem = (int) TemperatureProvider.getBlockTemperature(player.getLocation());
                        int stoneTem = 0;
                        stoneTem = Integer.parseInt(line);
                        double playerTem = PlayerManager.check(player, "tem");
                        if (playerTem < stoneTem)
                            PlayerApi.TemperatureOperations.changeTemperature(player, TemperatureChangeEvent.ChangeCause.WARMSTONE,1);
                        if (playerTem > stoneTem)
                            PlayerApi.TemperatureOperations.changeTemperature(player, TemperatureChangeEvent.ChangeCause.WARMSTONE,-1);
                        if (!getName(item).equalsIgnoreCase(ItemManager.items.get("WarmStone").displayName))
                            return true;
                        if (Math.random() <= stoneChangePercent) {
                            if (stoneTem > naturalTem)
                                line = "§8- §8§l温度 " + (stoneTem - 1);
                            if (stoneTem < naturalTem)
                                line = "§8- §8§l温度 " + (stoneTem + 1);
                            if (stoneTem == naturalTem)
                                line = "§8- §8§l温度 " + (stoneTem);
                            lores.set(line_line, line);
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

        public static void goNatural(Player player) {
            final double naturalTem = TemperatureProvider.getBlockTemperature(player.getLocation());
            final double playerTem = PlayerManager.check(player, PlayerManager.CheckType.TEMPERATURE);
            double check = naturalTem - playerTem;
            if (check == 0) return;
            if (check > 0) {
                if (!clothesChange(player, true)) return;
            } else if (!clothesChange(player, false)) return;
            double abs = Math.abs(check);
            if (abs < 1) {
                PlayerManager.change(player, PlayerManager.CheckType.TEMPERATURE, naturalTem, PlayerManager.EditAction.SET);
                return;
            }
            check = Math.max(-0.3, Math.min(0.3, check));
            if (abs > 10) {
                check += check * abs / 5;
            }
            PlayerApi.TemperatureOperations.changeTemperature(player, TemperatureChangeEvent.ChangeCause.ENVIRONMENT,check);
        }

        public static boolean clothesChange(Player player, boolean upOrDown) {
            double upFactor = 0.0;
            double downFactor = 0.0;
            PlayerInventory inv = player.getInventory();
            for (ItemStack item : inv.getArmorContents())
                if (clothesChangeTemperature.containsKey(getName(item))) {
                    if (clothesChangeTemperature.get(getName(item)) > 0) {
                        upFactor += clothesChangeTemperature.get(getName(item));
                    } else downFactor += clothesChangeTemperature.get(getName(item));
                }
            ItemStack[] clothes = ClothesContainer.getInventory(player).getStorageContents();
            for (ItemStack cloth : clothes) {
                if (clothesChangeTemperature.containsKey(getName(cloth))) {
                    if (clothesChangeTemperature.get(getName(cloth)) > 0) {
                        upFactor += clothesChangeTemperature.get(getName(cloth));
                    } else downFactor += clothesChangeTemperature.get(getName(cloth));
                }
            }
            if (upFactor == 2) return false;
            if (upOrDown) {
                return !(Math.random() < upFactor);
            } else {
                return !(Math.random() < downFactor);
            }
        }

        public static String getName(ItemStack item) {
            if (item != null)
                if (item.hasItemMeta())
                    if (item.getItemMeta().hasDisplayName())
                        return item.getItemMeta().getDisplayName();
            return "";
        }

        public ArrayList<UUID> hasStone = new ArrayList<>();
        public long totStone = 0;
        public long totNatural = 0;

        @Override
        public void run() {
            totStone++;
            totNatural++;
            if (totStone % temperatureChangeSpeedStone == 0) {
                totStone = 0;
                hasStone = new ArrayList<>();
                for (World world : Config.enableWorlds)
                    for (Player player : world.getPlayers())
                        if (!NPCChecker.isNPC(player))
                            if (goWarmStone(player))
                                hasStone.add(player.getUniqueId());
            }
            if (totNatural % temperatureChangeSpeedNatural == 0) {
                totNatural = 0;
                for (World world : Config.enableWorlds)
                    for (Player player : world.getPlayers())
                        if (!NPCChecker.isNPC(player))
                            if (!hasStone.contains(player.getUniqueId()))
                                goNatural(player);
            }
        }
    }
}