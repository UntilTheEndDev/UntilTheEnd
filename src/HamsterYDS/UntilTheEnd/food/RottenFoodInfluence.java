package HamsterYDS.UntilTheEnd.food;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class RottenFoodInfluence implements Listener{
	public static UntilTheEnd plugin;
	public RottenFoodInfluence(UntilTheEnd plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	private HashMap<String,Integer> eatenFoodRottens=new HashMap<String,Integer>();
	private HashMap<String,Integer> eatenFoodLevels=new HashMap<String,Integer>();
	@EventHandler public void onUse(PlayerItemConsumeEvent event) {
		ItemStack item=event.getItem();
		if(!item.getType().isEdible()) return;
		eatenFoodRottens.remove(event.getPlayer().getName());
		eatenFoodLevels.put(event.getPlayer().getName(),event.getPlayer().getFoodLevel());
		if(item.getType()==Material.ROTTEN_FLESH) eatenFoodRottens.put(event.getPlayer().getName(),-100);
		else eatenFoodRottens.put(event.getPlayer().getName(),RottenFoodTask.getRottenLevel(item));
	}
	@EventHandler public void onEat(FoodLevelChangeEvent event) {
		Entity entity=event.getEntity();
		if(!(entity instanceof Player)) return;
		if(eatenFoodRottens.containsKey(entity.getName())) {
			int level=eatenFoodRottens.get(entity.getName());
			if(level==-100) {
				PlayerManager.change(entity.getName(),"san",-30);
				LivingEntity creature=(LivingEntity) entity;
				creature.damage(2.0);
				creature.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,100,0));
				entity.sendMessage("§6[§cUntilTheEnd§6]§r 口区口区口区口区口区口区口区口区");
				event.setFoodLevel(eatenFoodLevels.get(entity.getName())-1);
				eatenFoodRottens.remove(entity.getName());
				return;
			}
			int currentLevel=eatenFoodLevels.get(entity.getName());
			int foodLevel=event.getFoodLevel()-currentLevel;
			double percent=level/100.0;
			int newLevel=(int) (percent*foodLevel+1.0);
			event.setFoodLevel(currentLevel+newLevel);
			if(level<=60){
				PlayerManager.change(entity.getName(),"san",(int)(-15.0D*(level/100)));
				entity.sendMessage("§6[§cUntilTheEnd§6]§r 食物貌似变味了~");
				eatenFoodRottens.remove(entity.getName());
			}
		}
	}
}
