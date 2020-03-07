package HamsterYDS.UntilTheEnd.cap.san;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.item.materials.NightMare;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class NightMareTask extends BukkitRunnable{
	public static UntilTheEnd plugin;
	public NightMareTask(UntilTheEnd plugin) {
		runTaskTimer(plugin,0L,600L); 
	}
	@Override
	public void run() {
		for(World world:Bukkit.getWorlds()) {
			if(!Config.enableWorlds.contains(world)) continue;
			for(Player player:world.getPlayers()) {
				int tot=0;
				PlayerInventory inv=player.getInventory();
				for(int slot=0;slot<=60;slot++) {
					if(inv.getItem(slot)==null) continue;
					ItemStack item=inv.getItem(slot).clone();
					item.setAmount(1);
					if(item.equals(NightMare.item)) {
						tot+=inv.getItem(slot).getAmount();
					}
				}
				PlayerManager.change(player.getName(),"san",-tot);
			}
		}
	}
}
