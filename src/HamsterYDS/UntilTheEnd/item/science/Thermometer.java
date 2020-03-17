package HamsterYDS.UntilTheEnd.item.science;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.cap.tem.TemperatureProvider;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Thermometer implements Listener{
	public Thermometer() {
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.items.get("Brick"),4);
		materials.put(ItemManager.items.get("电器元件"),3);
		materials.put(new ItemStack(Material.DIAMOND),2);
		ItemManager.items.get("").registerRecipe(materials,ItemManager.items.get("温度计"),"科学");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
		
		ItemManager.canPlaceBlocks.put("Thermometer",ItemManager.items.get("温度计"));
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
			final Hologram hologram=HologramsAPI.createHologram(ItemManager.plugin,loc.add(0.5,2.2,0.5).clone());
			hologram.appendTextLine(text);
			if(tem>=55)
				hologram.appendItemLine(new ItemStack(Material.BLAZE_POWDER));
			if(tem<=15)
				hologram.appendItemLine(new ItemStack(Material.ICE));
			new BukkitRunnable() {
				@Override
				public void run() {
					hologram.delete();
					clicked.remove(toString);
					cancel();
				}
			}.runTaskTimer(ItemManager.plugin,200,1);
		}
	}
}
