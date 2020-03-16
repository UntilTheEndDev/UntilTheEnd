package HamsterYDS.UntilTheEnd.item.clothes;

import java.util.HashMap;

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
	public static int sanityImprove = ItemManager.itemAttributes.getInt("眼球伞.sanityImprove");
	public static double range = ItemManager.itemAttributes.getDouble("眼球伞.range");
	public static int damageIncreasePeriod = ItemManager.itemAttributes.getInt("眼球伞.sdamageIncreasePeriod");

	public EyeballUmbrella() {
		HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
		materials.put(ItemManager.items.get("Rope"), 4);
		materials.put(ItemManager.items.get("Garland"), 1);
		materials.put(ItemManager.items.get("Spit"), 4);
		ItemManager.items.get("").registerRecipe(materials, ItemManager.items.get("眼球伞"), "衣物");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);

		ChangeTasks.clothesChangeSanity.put("眼球伞", sanityImprove);
		HamsterYDS.UntilTheEnd.cap.hum.ChangeTasks.umbrellas.add("眼球伞");
	}

	@EventHandler
	public void onLight(LightningStrikeEvent event) {
		Location loc = event.getLightning().getLocation();
		for (Entity entity : loc.getWorld().getNearbyEntities(loc, range, range, range)) {
			if (!(entity instanceof Player))
				continue;
			Player player = (Player) entity;
			ItemStack helmet = player.getInventory().getHelmet();
			if (helmet == null)
				continue;
			if (ItemManager.isSimilar(helmet.clone(), ItemManager.items.get("眼球伞"))) {
				event.setCancelled(true);
				player.sendMessage("[§cUntilTheEnd]§r 您的眼球伞成功吸引雷电一束！");
				player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, damageIncreasePeriod*20, 2));
			}
			break;
		}
	}
}
