package HamsterYDS.UntilTheEnd.item;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import HamsterYDS.UntilTheEnd.Config;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class ItemSawer extends BukkitRunnable{
	@Override
	public void run() {
		for(World world:Bukkit.getWorlds()) {
			if(Config.disableWorlds.contains(world.getName())) continue;
			for(Entity entity:world.getEntities()) {
				if(!(entity instanceof Item)) continue;
				if(!entity.isOnGround()) continue;
				try {
					Item item=(Item) entity;
					if(item.getItemStack().getItemMeta()==null) continue;
					if(item.getItemStack().getItemMeta().getDisplayName()==null) continue;
					Location where=entity.getLocation();
					String text=item.getItemStack().getItemMeta().getDisplayName();
					final Hologram hologram=HologramsAPI.createHologram(ItemLoader.plugin, where.add(0.0, 0.6, 0.0));
					hologram.appendTextLine(text);
					new BukkitRunnable() {
						@Override
						public void run() {
							hologram.delete();
							cancel();
						}
					}.runTaskTimer(ItemLoader.plugin,19L,1L);
				}catch(NoClassDefFoundError e) {}
			}
		}
	}
}
