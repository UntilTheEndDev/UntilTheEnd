package HamsterYDS.UntilTheEnd.item.combat;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.block.BlockManager;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.death.DeathCause;
import HamsterYDS.UntilTheEnd.player.death.DeathMessage;

public class BeeMine implements Listener {
	public static float explosionAmount = ItemManager.yaml2.getInt("蜜蜂地雷.explosionAmount");
	public static double poison = ItemManager.yaml2.getDouble("蜜蜂地雷.poison");

	public BeeMine() {
		HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
		materials.put(ItemManager.namesAndItems.get("§6板条"), 2);
		materials.put(new ItemStack(Material.SUGAR), 3);
		materials.put(new ItemStack(Material.FLINT), 2);
		materials.put(new ItemStack(Material.GOLD_PLATE), 1);
		ItemManager.registerRecipe(materials, ItemManager.namesAndItems.get("§6蜜蜂地雷"), "§6战斗");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
		ItemManager.canPlaceBlocks.put("BeeMine", ItemManager.namesAndItems.get("§6蜜蜂地雷"));
	}

	@EventHandler
	public void onInteract(EntityInteractEvent event) {
		if (event.isCancelled())
			return;
		Entity entity = event.getEntity();
		Location loc = event.getBlock().getLocation();
		if (!(entity instanceof LivingEntity))
			return;
		if (BlockApi.getSpecialBlocks("BeeMine").contains(BlockApi.locToStr(loc))) {
			BlockManager.blocks.remove(BlockApi.locToStr(loc));
			BlockManager.removeBlockData("BeeMine", BlockApi.locToStr(loc));
			entity.getWorld().createExplosion(loc.getX() + 0.5, loc.getY() + 0.5, loc.getZ() + 0.5, explosionAmount,
					false, false);
			((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int) poison * 20, 0));
			loc.getBlock().breakNaturally(new ItemStack(Material.AIR));
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
			return;
		Location loc = event.getTo();
		if (BlockApi.getSpecialBlocks("BeeMine").contains(BlockApi.locToStr(loc))) {
			BlockManager.blocks.remove(BlockApi.locToStr(loc));
			BlockManager.removeBlockData("BeeMine", BlockApi.locToStr(loc));
			player.getWorld().createExplosion(loc.getX() + 0.5, loc.getY() + 0.5, loc.getZ() + 0.5, explosionAmount,
					false, false);
			((LivingEntity) player).addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int) poison * 20, 0));
			if (player.getHealth() <= 3)
				DeathMessage.causes.put(player.getName(), DeathCause.BEEMINE);
			loc.getBlock().breakNaturally(new ItemStack(Material.AIR));
		}
	}
}
