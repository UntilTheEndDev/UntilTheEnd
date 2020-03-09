package HamsterYDS.UntilTheEnd.cap.tiredness;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;

public class ChangeTasks {
	public UntilTheEnd plugin;
	public ChangeTasks(UntilTheEnd plugin) {
		this.plugin=plugin;
		new Behaviors().runTaskTimer(plugin,0L,40L);
	}
	public class Behaviors extends BukkitRunnable{

		@Override
		public void run() {
			for(World world:Config.enableWorlds)
				for(Player player:world.getPlayers()) {
					if(player.isSprinting()) 
						PlayerManager.change(player,CheckType.TIREDNESS,2);
					if(player.isInsideVehicle()) 
						PlayerManager.change(player,CheckType.TIREDNESS,-2);
					if(player.isSleeping()) 
						PlayerManager.change(player,CheckType.TIREDNESS,-4);
					if(player.isBlocking()) 
						PlayerManager.change(player,CheckType.TIREDNESS,2);
					if(player.isGliding()) 
						PlayerManager.change(player,CheckType.TIREDNESS,3);
					if(!ChangeEvents.movingPlayers.contains(player.getUniqueId()))
						PlayerManager.change(player,CheckType.TIREDNESS,-1);
					else 
						PlayerManager.change(player,CheckType.TIREDNESS,1);
					PlayerInventory inv=player.getInventory();
					int tot=0;
					for(ItemStack item:inv.getContents()) {
						if(item==null) continue;
						tot+=item.getAmount(); //TODO
					}
					PlayerManager.change(player,CheckType.TIREDNESS,tot/100);
				}
						
		}
		
	}
}
