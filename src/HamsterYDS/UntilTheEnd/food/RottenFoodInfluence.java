package HamsterYDS.UntilTheEnd.food;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
	private HashMap<String,Integer> eatenFoodLevels=new HashMap<String,Integer>();
	@EventHandler public void onUse(PlayerItemConsumeEvent event) {
		ItemStack item=event.getItem();
		if(!item.getType().isEdible()) return;
		if(item.getType().equals(Material.ROTTEN_FLESH))
			eatenFoodLevels.put(event.getPlayer().getName(),-100);
		int level=RottenFoodTask.getRottenLevel(item);
		System.out.println(level);
		eatenFoodLevels.put(event.getPlayer().getName(),level);
	}
	@EventHandler public void onEat(FoodLevelChangeEvent event) {
		Entity entity=event.getEntity();
		if(eatenFoodLevels.containsKey(entity.getName())) {
			int level=eatenFoodLevels.get(entity.getName());
			if(level==-100) {
				PlayerManager.change(entity.getName(),"san",-30);
				LivingEntity creature=(LivingEntity) entity;
				creature.damage(2.0);
				creature.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,100,0));
				entity.sendMessage("§6[§cUntilTheEnd§6]§r 口区口区口区口区口区口区口区口区");
			}
			if(level<=60)
				PlayerManager.change(entity.getName(),"san",(int)(-15.0D*(level/200)));
			event.setFoodLevel((int) (((double)event.getFoodLevel())*((double)(level/200.0))+1));
			if(level<=60)
				entity.sendMessage("§6[§cUntilTheEnd§6]§r 食物貌似变味了~");
			eatenFoodLevels.remove(entity.getName());
		}
	}
}
