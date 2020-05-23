package HamsterYDS.UntilTheEnd.item.science;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.cap.tem.TemperatureProvider;
import HamsterYDS.UntilTheEnd.event.block.CustomBlockPlaceEvent;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class ColdFire implements Listener {
	public ColdFire() {
		TemperatureProvider.uteBlockTemperatures.put("ColdFire", 0);
		Bukkit.getPluginManager().registerEvents(this, UntilTheEnd.getInstance());
	}

	@EventHandler
	public void onPlace(CustomBlockPlaceEvent event) {
		if (event.getItem().id.equalsIgnoreCase("ColdFire")) {
			new BukkitRunnable() {
				int counter = 0;

				@Override
				public void run() {
					if (counter >= 90) {
						event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),
								ItemManager.items.get("Ashes").item.clone());
						event.getBlock().setType(Material.AIR);
						cancel();
					}
					counter++;
					event.getBlock().getWorld().spawnParticle(Particle.CRIT_MAGIC,
							event.getBlock().getLocation().add(0.5, 0.5, 0.5), 3);
				}
			}.runTaskTimer(UntilTheEnd.getInstance(), 0L, 20L);
		}
	}

	@EventHandler
	public void onRight(PlayerInteractEvent event) {
		if (event.hasItem()) {
			ItemStack item = event.getItem();
			if (ItemManager.getUTEItemId(item).equalsIgnoreCase("ColdFire")) {
				BlockFace face = event.getBlockFace();
				Location loc = new Location(event.getClickedBlock().getWorld(), face.getModX(), face.getModY(),
						face.getModZ());
				if (loc.getBlock().getType() == Material.AIR) {
					loc.getBlock().setType(Material.FIRE);
				}
			}
		}
	}
}
