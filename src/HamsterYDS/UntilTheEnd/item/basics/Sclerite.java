package HamsterYDS.UntilTheEnd.item.basics;

import org.bukkit.Material;
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
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		ItemStack item = player.getInventory().getItemInMainHand();
		if (ItemManager.isSimilar(item, ItemManager.namesAndItems.get("§6骨片"))) {
			Vector vec = player.getEyeLocation().getDirection().multiply(0.5);
			Entity entity = player.getWorld().spawnEntity(player.getLocation().add(0, 1.5, 0), EntityType.ARMOR_STAND);
			ArmorStand armor = (ArmorStand) entity;
			armor.setItemInHand(new ItemStack(Material.PRISMARINE_CRYSTALS));
			armor.setInvulnerable(true);
			armor.setVisible(false);
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
						cancel();
					}
					if(armor.getLocation().getBlock().getType().isTransparent())
						cancel();
					if (dist++ >= maxDist)
						cancel();
				}
			}.runTaskTimer(ItemManager.plugin, 0L, 1L);
		}
	}
}
