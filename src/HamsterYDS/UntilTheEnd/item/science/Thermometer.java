package HamsterYDS.UntilTheEnd.item.science;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.cap.tem.TemperatureProvider;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Thermometer implements Listener{
	public static int existPeriod=ItemManager.itemAttributes.getInt("Thermometer.existPeriod"); 
	public Thermometer() {
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
	}
	ArrayList<String> clicked=new ArrayList<String>();
	@EventHandler 
	public void onClick(PlayerInteractEvent event) {
		if(event.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
		Player player=event.getPlayer();
		Block block=event.getClickedBlock();
		Location loc=block.getLocation();
		String toString=BlockApi.locToStr(loc);
		if(UntilTheEndApi.BlockApi.getSpecialBlocks("Thermometer").contains(toString)) {
			if(clicked.contains(toString)) return;
			clicked.add(toString);
			int tem=(int)TemperatureProvider.getBlockTemperature(loc);
			String text="§e§l温度§d§l"+tem+"§e§l°C";
			ArmorStand armor=(ArmorStand) player.getWorld().spawnEntity(loc.clone().add(0.5,0.5,0.5),EntityType.ARMOR_STAND);
			armor.setVisible(false);
			armor.setSmall(true);
			armor.setGravity(false);
			armor.setCustomName(text);
			armor.setCustomNameVisible(true);
			new BukkitRunnable() {
				@Override
				public void run() {
					armor.remove();
					clicked.remove(toString);
					cancel();
				}
			}.runTaskTimer(ItemManager.plugin,existPeriod*20,1);
		}
	}
}
