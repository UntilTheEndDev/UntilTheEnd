package ute.item.clothes;

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

public class EyeballUmbrella implements Listener {
    public static int sanityImprove = ItemManager.itemAttributes.getInt("EyeballUmbrella.sanityImprove");
    public static double range = ItemManager.itemAttributes.getDouble("EyeballUmbrella.range");
    public static int damageIncreasePeriod = ItemManager.itemAttributes.getInt("EyeballUmbrella.sdamageIncreasePeriod");

    public EyeballUmbrella() {
        ChangeTasks.clothesChangeSanity.put(ItemManager.items.get("EyeballUmbrella").displayName, sanityImprove);
        ute.cap.hum.ChangeTasks.umbrellas.add(ItemManager.items.get("EyeballUmbrella").displayName);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onLight(LightningStrikeEvent event) {
        if (!Config.enableWorlds.contains(event.getLightning().getWorld())) return;
        Location loc = event.getLightning().getLocation();
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, range, range, range)) {
            if (!(entity instanceof Player))
                continue;
            Player player = (Player) entity;
            ItemStack helmet = player.getInventory().getHelmet();
            if (ItemManager.isSimilar(helmet, getClass())) {
                event.setCancelled(true);
                player.sendMessage("[§cUntilTheEnd]§r 您的眼球伞成功吸引雷电一束！");
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, damageIncreasePeriod * 20, 2));
            }
            ItemStack[] clothes = ClothesContainer.getInventory(player).getStorageContents();
            for (ItemStack cloth : clothes)
                if (ItemManager.isSimilar(cloth, getClass())) {
                    event.setCancelled(true);
                    player.sendMessage("[§cUntilTheEnd]§r 您的眼球伞成功吸引雷电一束！");
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, damageIncreasePeriod * 20, 2));
                }
            break;
        }
    }
}
