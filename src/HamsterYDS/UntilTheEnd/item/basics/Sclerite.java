package HamsterYDS.UntilTheEnd.item.basics;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Sclerite implements Listener {
	public static double damage = ItemManager.yaml2.getDouble("骨片.damage");
	public static double range = ItemManager.yaml2.getDouble("骨片.range");
	public static double maxDist = ItemManager.yaml2.getDouble("骨片.maxDist");

	public Sclerite() {
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
		ItemManager.cosumeItems.add("Sclerite");
	}

	@EventHandler
	public void onRight(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!player.isSneaking())
			return;
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ItemStack item = player.getInventory().getItemInMainHand();
			if (ItemManager.isSimilar(item, ItemManager.namesAndItems.get("§6骨片"))) {
				event.setCancelled(true);
				Vector vec = player.getEyeLocation().getDirection().multiply(10);
				Entity entity = player.getWorld().spawnEntity(player.getLocation().add(0, 1, 0),
						EntityType.ARMOR_STAND);
				ArmorStand armor = (ArmorStand) entity;
				armor.setSmall(true);
				armor.setItemInHand(new ItemStack(Material.PRISMARINE_CRYSTALS));
				armor.setCollidable(false);
				armor.setVisible(false);
				armor.setAI(false);
				new BukkitRunnable() {
					int dist = 0;

					@Override
					public void run() {
						for (int i = 0; i <= 5; i++)
							armor.setVelocity(vec);

						for (Entity entity : armor.getNearbyEntities(range, range, range)) {
							if (entity.getUniqueId() == player.getUniqueId())
								continue;
							if (!(entity instanceof LivingEntity))
								continue;
							((LivingEntity) entity).damage(damage);
							clear();
						}

						if (!armor.getLocation().add(0, 0.2, 0).getBlock().getType().isTransparent()) {
							armor.setGravity(false);
							armor.setVelocity(vec);
							armor.getWorld().spawnParticle(Particle.CRIT, armor.getLocation().add(0, 0.2, 0), 1);
							clear();
						}
						if (dist++ >= maxDist)
							clear();
					}

					public void clear() {
						new BukkitRunnable() {
							@Override
							public void run() {
								armor.remove();
								cancel();
							}
						}.runTaskTimer(ItemManager.plugin,
								ItemManager.plugin.getConfig().getInt("item.blowarrow.autoclear") * 20, 20L);
						cancel();
					}
				}.runTaskTimer(ItemManager.plugin, 0L, 1L);
			}
		}
	}
}
