package HamsterYDS.UntilTheEnd.food;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class RottenFood3 implements Listener{
	public static UntilTheEnd plugin;
	public RottenFood3(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	private ArrayList<Integer> eaters=new ArrayList<Integer>();
	private HashMap<Integer,Integer> levels=new HashMap<Integer,Integer>();
	@EventHandler public void onEat1(PlayerItemConsumeEvent event) {
		ItemStack item=event.getItem();
		if(!item.getType().isEdible()) return;
		if(item.getType().equals(Material.ROTTEN_FLESH))
			eaters.add(event.getPlayer().getEntityId());
		int level=RottenFood1.getRot(item);
		levels.put(event.getPlayer().getEntityId(),level);
	}
	@EventHandler public void onEat2(FoodLevelChangeEvent event) {
		Entity entity=event.getEntity();
		if(eaters.contains(entity.getEntityId())) {
			PlayerManager.change(entity.getName(),"san",-(int) (15*Math.random()));
			event.setFoodLevel((int)(event.getFoodLevel()*0.5));
			entity.sendMessage("§6[§c凌域§6]§r 食物貌似变味了~");
			eaters.remove((Integer)entity.getEntityId());
		}
	}
	@EventHandler public void onEat3(FoodLevelChangeEvent event) {
		Entity entity=event.getEntity();
		if(levels.containsKey(entity.getEntityId())) {
			int level=levels.get(entity.getEntityId());
			event.setFoodLevel((int) (event.getFoodLevel()*(double)(level/100)));
		}
	}
}
