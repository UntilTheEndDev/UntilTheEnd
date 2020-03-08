package HamsterYDS.UntilTheEnd.cap.hum;

import java.util.ArrayList;
import java.util.List;

import HamsterYDS.UntilTheEnd.manager.WetManager;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

import javax.jws.WebMethod;

public class InfluenceTasks {
    public static long dampPeriod = Humidity.yaml.getLong("dampPeriod");
    public static double dampPercent = Humidity.yaml.getDouble("dampPercent");
    public static long seasonPeriod = Humidity.yaml.getLong("seasonPeriod");
    public static double seasonPercent = Humidity.yaml.getDouble("seasonPercent");
    public static int effectHumidity = Humidity.yaml.getInt("effectHumidity");
    public static int seasonHumidity = Humidity.yaml.getInt("seasonHumidity");
    public static int dampHumidity = Humidity.yaml.getInt("dampHumidity");

    public InfluenceTasks(UntilTheEnd plugin) {
        new Effect().runTaskTimer(plugin, 0L, 200L);
        new NormalDamp().runTaskTimer(plugin, 0L, dampPeriod);
        new NormalSeason().runTaskTimer(plugin, 0L, seasonPeriod);
    }

    public static class Effect extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds) {
                for (Player player : world.getPlayers()) {
                    int hum = PlayerManager.check(player, PlayerManager.CheckType.HUMIDITY);
                    if (hum <= effectHumidity) continue;
                    if (!player.hasPotionEffect(PotionEffectType.SLOW_DIGGING))
                        player.sendMessage("§6[§cUntilTheEnd§6]§r 我全身都淋湿了！");  //Language-TODO
                    player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 300, 1));
                }
            }
        }
    }

    public static class NormalDamp extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds) {
                if (world.hasStorm())
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof Item) {
                            Item entityItem = (Item) entity;
                            if (entity.getLocation().getBlock().getTemperature() > 1) continue;
                            ItemStack item = entityItem.getItemStack();
                            if ((!WetManager.isWet(item)) && Math.random() <= dampPercent) {
                                WetManager.setWet(item, true);
                            }
                        }
                    }
                for (Player player : world.getPlayers()) {
                    int hum = PlayerManager.check(player, PlayerManager.CheckType.HUMIDITY);
                    if (hum <= dampHumidity) continue;
                    double wetLevel = 0.05 + (hum - 10) * 0.01;
                    PlayerInventory inv = player.getInventory();
                    for (int slot = 0; slot < inv.getSize(); slot++) {
                        ItemStack item = inv.getItem(slot);
                        if (item == null) continue;
                        if ((!WetManager.isWet(item)) && Math.random() <= wetLevel) {
                            WetManager.setWet(item, true);
                            inv.setItem(slot, item);
                        }
                    }
                }
            }
        }
    }

    public static class NormalSeason extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds) {
                if (!world.hasStorm())
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof Item) {
                            Item entityItem = (Item) entity;
                            ItemStack item = entityItem.getItemStack();
                            if (item == null) continue;
                            if (!WetManager.isWet(item)) continue;
                            if (Math.random() <= seasonPercent) {
                                WetManager.setWet(item, false);
                                entityItem.setItemStack(item);
                            }
                        }
                    }
                for (Player player : world.getPlayers()) {
                    int hum = PlayerManager.check(player, PlayerManager.CheckType.HUMIDITY);
                    if (hum > seasonHumidity) continue;
                    PlayerInventory inv = player.getInventory();
                    for (int slot = 0; slot < inv.getSize(); slot++) {
                        ItemStack item = inv.getItem(slot);
                        if (item == null) continue;
                        if (!WetManager.isWet(item)) continue;

                        if (Math.random() <= seasonPercent) {
                            WetManager.setWet(item, false);
                            inv.setItem(slot, item);
                        }
                    }
                }
            }
        }
    }
}
