package ute.item.clothes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import ute.Config;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ute.cap.san.ChangeTasks;
import ute.item.ItemManager;

import java.io.File;

public class EyeballUmbrella implements Listener {
    public static int sanityImprove = ItemManager.itemAttributes.getInt("EyeballUmbrella.sanityImprove");
    public static double range = ItemManager.itemAttributes.getDouble("EyeballUmbrella.range");
    public static int damageIncreasePeriod = ItemManager.itemAttributes.getInt("EyeballUmbrella.damageIncreasePeriod");

    public EyeballUmbrella() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
        ChangeTasks.clothesChangeSanity.put(ItemManager.items.get("EyeballUmbrella").displayName, sanityImprove);
        ute.cap.hum.ChangeTasks.umbrellas.add(ItemManager.items.get("EyeballUmbrella").displayName);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLight(LightningStrikeEvent event) {
        if (!Config.enableWorlds.contains(event.getLightning().getWorld())) return;
        Location loc = event.getLightning().getLocation();
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, range, range, range)) {
            if (!(entity instanceof Player))
                continue;
            Player player = (Player) entity;
            ItemStack helmet = player.getInventory().getHelmet();
            if (ItemManager.isSimilar(helmet, getClass())) {
                if (helmet.getDurability() >= helmet.getType().getMaxDurability())
                    helmet.setType(Material.AIR);
                if (Math.random() <= 0.5)
                    helmet.setDurability((short) (helmet.getDurability() + 1));
                
                event.setCancelled(true);
                player.sendMessage("§c[UntilTheEnd]§r 您的眼球伞成功吸引雷电一束！");
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, damageIncreasePeriod * 20, 2));
                return;
            }
            if(ClothesContainer.getInventory(player)==null) {
                File file = new File(ItemManager.plugin.getDataFolder() + "/clothes", player.getUniqueId().toString() + ".yml");
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                Inventory inv = Bukkit.createInventory(player, 9, ItemManager.items.get("ClothesContainer").displayName);
                for (String path : yaml.getKeys(false)) {
                    ItemStack item = yaml.getItemStack(path);
                    inv.setItem(Integer.parseInt(path), item);
                }
                ClothesContainer.invs.put(player.getUniqueId(), inv);
            }
            ItemStack[] clothes = ClothesContainer.getInventory(player).getStorageContents();
            for (ItemStack cloth : clothes)
                if (ItemManager.isSimilar(cloth, getClass())) {

                    if (cloth.getDurability() >= cloth.getType().getMaxDurability())
                        cloth.setType(Material.AIR);
                    if (Math.random() <= 0.5)
                        cloth.setDurability((short) (cloth.getDurability() + 1));
                    
                    event.setCancelled(true);
                    player.sendMessage("§c[UntilTheEnd]§r 您的眼球伞成功吸引雷电一束！");
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, damageIncreasePeriod * 20, 2));
                }
            break;
        }
    }
}
