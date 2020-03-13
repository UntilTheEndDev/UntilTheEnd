package HamsterYDS.UntilTheEnd.food;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Food {
	public static UntilTheEnd plugin;
	public Food(UntilTheEnd plugin) {
		this.plugin=plugin;
		if(plugin.getConfig().getBoolean("food.rotten.invenable"))
			new RottenFoodTask(plugin);
		if(plugin.getConfig().getBoolean("food.rotten.guienable"))
			plugin.getServer().getPluginManager().registerEvents(new RottenFoodEvents(),plugin);
		new RottenFoodInfluence(plugin);
		if(plugin.getConfig().getBoolean("food.hunger.enable"))
			new Hunger(plugin);
	}
}
