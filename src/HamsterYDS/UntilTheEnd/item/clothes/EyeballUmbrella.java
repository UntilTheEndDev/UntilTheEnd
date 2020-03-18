package HamsterYDS.UntilTheEnd.item.clothes;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import HamsterYDS.UntilTheEnd.cap.san.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class EyeballUmbrella implements Listener {
	public static int sanityImprove = ItemManager.itemAttributes.getInt("EyeballUmbrella.sanityImprove");
	public static double range = ItemManager.itemAttributes.getDouble("EyeballUmbrella.range");
	public static int damageIncreasePeriod = ItemManager.itemAttributes.getInt("EyeballUmbrella.sdamageIncreasePeriod");

	public EyeballUmbrella() {
		ChangeTasks.clothesChangeSanity.put(ItemManager.items.get("EyeballUmbrella").displayName, sanityImprove);
		HamsterYDS.UntilTheEnd.cap.hum.ChangeTasks.umbrellas.add(ItemManager.items.get("EyeballUmbrella").displayName);
	}

	@EventHandler
	public void onLight(LightningStrikeEvent event) {
		Location loc = event.getLightning().getLocation();
		for (Entity entity : loc.getWorld().getNearbyEntities(loc, range, range, range)) {
			if (!(entity instanceof Player))
				continue;
			Player player = (Player) entity;
			ItemStack helmet = player.getInventory().getHelmet();
			if (ItemManager.isSimilar(helmet,getClass())) {
				event.setCancelled(true);
				player.sendMessage("[§cUntilTheEnd]§r 您的眼球伞成功吸引雷电一束！");
				player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, damageIncreasePeriod*20, 2));
			}
			break;
		}
	}
}
