package HamsterYDS.UntilTheEnd.cap.hum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import HamsterYDS.UntilTheEnd.cap.tem.TemperatureProvider;
import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

public class ChangeTasks {
    public static UntilTheEnd plugin;
    public static ArrayList<String> umbrellas = new ArrayList<String>();
    public static ArrayList<String> waterProofSuits = new ArrayList<String>();
    public static long weahterChangePeriod = Humidity.yaml.getLong("weahterChangePeriod");
    public static long stateChangePeriod = Humidity.yaml.getLong("stateChangePeriod");
    public static int stormSlowLevel = Humidity.yaml.getInt("stormSlowLevel", 5);
    public static Map<UUID, Number> stepStatus = new HashMap<>();
    private static final int highest;

    static {
        int h = 0;
        //noinspection NumberEquality
        do {
            h++;
        } while (Integer.valueOf(h) != Integer.valueOf(h));
        highest = h - 1;
    }

    public static final BiFunction<UUID, Number, Number> step_reset = (uuid, old) -> {
        if (old == null) return 0;
        if (old instanceof AtomicInteger) {
            ((AtomicInteger) old).set(0);
            return old;
        }
        return 0;
    };
    public static final BiFunction<UUID, Number, Number> step_status_changer = (uuid, old) -> {
        if (old == null) return 0;
        if (old instanceof AtomicInteger) {
            ((AtomicInteger) old).incrementAndGet();
            return old;
        }
        int val = old.intValue();
        if (val < highest) {
            return val + 1;
        }
        return new AtomicInteger(val + 1);
    };

    public ChangeTasks(UntilTheEnd plugin) {
        ChangeTasks.plugin = plugin;
        new WeatherTask().runTaskTimer(plugin, 0L, weahterChangePeriod);
        new StateTask().runTaskTimer(plugin, 0L, stateChangePeriod);
    }

    public static class WeatherTask extends BukkitRunnable {
        public static void doTickTem(Player player) {
            final Location location = player.getLocation();
            final Block block = location.getBlock();
            int rm = 10;
            if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
                rm += 15;
            }
            double tmp = TemperatureProvider.getBlockTemperature(location);
            double pt = PlayerManager.check(player, PlayerManager.CheckType.TEMPERATURE);
            if (tmp > pt) {
                PlayerManager.change(player, PlayerManager.CheckType.HUMIDITY, (pt - tmp) / rm);
            }
        }

        @Override
        public void run() {
            for (World world : Config.enableWorlds) {
                if (world.hasStorm()) {
                    player_loop:
                    for (Player player : world.getPlayers()) {
                        if (NPCChecker.isNPC(player)) continue;
                        // 莫得雨
                        if (player.getLocation().getBlock().getTemperature() > 1.0
                                || player.getLocation().getBlock().getBiome().toString().contains("SAVANNA")) {
                            PlayerManager.change(player, PlayerManager.CheckType.HUMIDITY, -1);
                            doTickTem(player);
                            continue;
                        }
                        PlayerInventory inv = player.getInventory();
                        ItemStack rightHand = inv.getItemInMainHand();
                        ItemStack leftHand = inv.getItemInOffHand();
                        if (hasShelter(player)) {
                            doTickStorm(player);
                            doTickTem(player);
                            continue;
                        }
                        if (isUmbrella(rightHand) || isUmbrella(leftHand)) continue;
                        ItemStack[] armors = inv.getArmorContents();
                        for (ItemStack armor : armors) {
                            if (isSuit(armor)) {
                                doTickStorm(player);
                                doTickTem(player);
                                continue player_loop;
                            }
                        }
                        PlayerManager.change(player, PlayerManager.CheckType.HUMIDITY, 0.3);
                    }
                } else {
                    for (Player player : world.getPlayers()) {
                        if (NPCChecker.isNPC(player)) continue;
                        doTickTem(player);
                        PlayerManager.change(player, PlayerManager.CheckType.HUMIDITY, -0.3);
                    }
                }
            }
        }

        public static void doTickStorm(Player p) {
            if (stepStatus.compute(p.getUniqueId(), step_status_changer).intValue() >= stormSlowLevel) {
                stepStatus.compute(p.getUniqueId(), step_reset);
                PlayerManager.change(p, PlayerManager.CheckType.HUMIDITY, -1);
            }
        }

        public static boolean isUmbrella(ItemStack item) {
            if (item == null) return false;
            if (item.hasItemMeta())
                if (item.getItemMeta().hasDisplayName())
                    if (umbrellas.contains(item.getItemMeta().getDisplayName()))
                        return true;
            return false;
        }

        public static boolean isSuit(ItemStack item) {
            if (item == null) return false;
            if (item.hasItemMeta())
                if (item.getItemMeta().hasDisplayName())
                    if (waterProofSuits.contains(item.getItemMeta().getDisplayName()))
                        return true;
            return false;
        }

        public static boolean hasShelter(Entity entity) {
            Location loc = entity.getLocation().add(0, 1, 0);
            for (int i = 0; i <= 100; loc = loc.add(0, 1.0, 0), i++)
                if (entity.getWorld().getBlockAt(loc).getType() != Material.AIR)
                    return true;
            return false;
        }
    }

    public static class StateTask extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds) {
                playerLoop:
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)) continue;
                    if (player.isInsideVehicle()) {
                        Location loc = player.getLocation().add(0, 1, 0); // 沉了
                        if (world.getBlockAt(loc).getType().equals(Material.WATER) || world.getBlockAt(loc).getType().equals(Material.STATIONARY_WATER))
                            PlayerManager.change(player, PlayerManager.CheckType.HUMIDITY, 1);
                        continue;
                    }
                    Location loc = player.getLocation();
                    if (world.getBlockAt(loc).getType().equals(Material.WATER) || world.getBlockAt(loc).getType().equals(Material.STATIONARY_WATER)) {
                        ItemStack[] armors = player.getInventory().getArmorContents();
                        for (ItemStack armor : armors) {
                            if (isSuit(armor)) {
                                continue playerLoop;
                            }
                        }
                        PlayerManager.change(player, PlayerManager.CheckType.HUMIDITY, 1);
                    }
                }
            }
        }

        public static boolean isSuit(ItemStack item) {
            if (item == null) return false;
            if (item.hasItemMeta())
                if (item.getItemMeta().hasDisplayName())
                    if (waterProofSuits.contains(item.getItemMeta().getDisplayName()))
                        return true;
            return false;
        }
    }
}
