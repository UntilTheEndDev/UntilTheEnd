package ute.cap.san;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import ute.Config;
import ute.UntilTheEnd;
import ute.api.PlayerApi;
import ute.api.event.cap.SanityChangeEvent;
import ute.internal.NPCChecker;
import ute.internal.ResidenceChecker;
import ute.item.clothes.ClothesContainer;
import ute.player.PlayerManager;

import java.util.HashMap;

public class ChangeTasks {
    public static UntilTheEnd plugin = UntilTheEnd.getInstance();
    public static double auraRangeX = Sanity.yaml.getDouble("auraRangeX");
    public static double auraRangeY = Sanity.yaml.getDouble("auraRangeY");
    public static double auraRangeZ = Sanity.yaml.getDouble("auraRangeZ");
    public static int monsterChangeSanity = Sanity.yaml.getInt("monsterChangeSanity");
    public static int playerChangeSanity = Sanity.yaml.getInt("playerChangeSanity");
    public static int auraPeriod = Sanity.yaml.getInt("auraPeriod");
    public static int humidityPeriod = Sanity.yaml.getInt("humidityPeriod");
    public static int timePeriod = Sanity.yaml.getInt("timePeriod");
    public static int evening = Sanity.yaml.getInt("evening");
    public static int night = Sanity.yaml.getInt("night");
    public static int day = Sanity.yaml.getInt("day");
    public static HashMap<String, Integer> clothesChangeSanity = new HashMap<String, Integer>();
    public static HashMap<String, Integer> itemsChangeSanity = new HashMap<String, Integer>();

    public static void initialize() {
        new ClothesTasks().runTaskTimer(plugin, 0L, 200L);
        new ItemsTasks().runTaskTimer(plugin, 0L, 200L);
        new SanityAura().runTaskTimer(plugin, 0L, auraPeriod);
        new HumidityTask().runTaskTimer(plugin, 0L, humidityPeriod);
        new TimeTask().runTaskTimer(plugin, 0L, timePeriod);
    }

    public static class ClothesTasks extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds) {
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)|| ResidenceChecker.isProtected(player.getLocation())) continue;
                    PlayerInventory inv = player.getInventory();
                    double change = 0.0;
                    
                    for(ItemStack armor:inv.getArmorContents()){
                        if(clothesChangeSanity.containsKey(getName(armor))){
                            if (armor.getDurability() >= armor.getType().getMaxDurability())
                                armor.setType(Material.AIR);
                            if (Math.random() <= 0.01)
                                armor.setDurability((short) (armor.getDurability() + 1));

                            change += clothesChangeSanity.get(getName(armor));
                        }
                    }
                    
                    ItemStack[] clothes = ClothesContainer.getInventory(player).getStorageContents();
                    for (ItemStack cloth : clothes) {
                        if (clothesChangeSanity.containsKey(getName(cloth))) {

                            if (cloth.getDurability() >= cloth.getType().getMaxDurability())
                                cloth.setType(Material.AIR);
                            if (Math.random() <= 0.01)
                                cloth.setDurability((short) (cloth.getDurability() + 1));
                            
                            change += clothesChangeSanity.get(getName(cloth));
                        }
                    }
                    PlayerApi.SanityOperations.changeSanity(player, SanityChangeEvent.ChangeCause.INVENTORYCLOTHES,change);
                }
            }
        }

        public String getName(ItemStack item) {
            if (item != null)
                if (item.hasItemMeta())
                    if (item.getItemMeta().hasDisplayName())
                        return item.getItemMeta().getDisplayName();
            return "";
        }
    }

    public static class ItemsTasks extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds) {
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)||ResidenceChecker.isProtected(player.getLocation())) continue;
                    PlayerInventory inv = player.getInventory();
                    for (int slot = 0; slot < inv.getSize(); slot++) {
                        ItemStack item = inv.getItem(slot);
                        String itemName = getName(item);
                        if (itemsChangeSanity.containsKey(itemName)) {
                            PlayerApi.SanityOperations.changeSanity(player, SanityChangeEvent.ChangeCause.INVENTORYITEM,item.getAmount() * itemsChangeSanity.get(itemName));
                        }
                    }
                }
            }
        }

        public String getName(ItemStack item) {
            if (item != null)
                if (item.hasItemMeta())
                    if (item.getItemMeta().hasDisplayName())
                        return item.getItemMeta().getDisplayName();
            return "";
        }
    }

    public static class SanityAura extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers())
                    if (!NPCChecker.isNPC(player))
                        for (Entity entity : player.getNearbyEntities(auraRangeX, auraRangeY, auraRangeZ)) {
                            EntityType type = entity.getType();
                            if (SanityProvider.creatureAura.containsKey(type))
                                PlayerApi.SanityOperations.changeSanity(player, SanityChangeEvent.ChangeCause.CREATUREAURA,SanityProvider.creatureAura.get(type));
                            if (entity instanceof Player)
                                PlayerApi.SanityOperations.changeSanity(player, SanityChangeEvent.ChangeCause.PLAYER,playerChangeSanity);
                            if (entity instanceof Monster)
                                PlayerApi.SanityOperations.changeSanity(player, SanityChangeEvent.ChangeCause.MONSTER,monsterChangeSanity);
                        }
        }
    }

    public static class HumidityTask extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds) {
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)||ResidenceChecker.isProtected(player.getLocation())) continue;
                    double hum = PlayerManager.check(player, PlayerManager.CheckType.HUMIDITY);
                    PlayerApi.SanityOperations.changeSanity(player, SanityChangeEvent.ChangeCause.HUMIDITY,-hum / 10);
                }
            }
        }
    }

    public static class TimeTask extends BukkitRunnable {
        public long counter = 0;

        @Override
        public void run() {
            counter++;
            for (World world : Config.enableWorlds) {
                long time = world.getTime();
                if (counter % 2 == 0)
                    if (time >= evening && time <= night)
                        for (Player player : world.getPlayers())
                            if (!NPCChecker.isNPC(player))
                                PlayerApi.SanityOperations.changeSanity(player, SanityChangeEvent.ChangeCause.EVENING,-1);
                if (time >= night && time <= day)
                    for (Player player : world.getPlayers())
                        if (!NPCChecker.isNPC(player))
                            PlayerApi.SanityOperations.changeSanity(player, SanityChangeEvent.ChangeCause.NIGHT,-1);
            }
        }
    }
}
