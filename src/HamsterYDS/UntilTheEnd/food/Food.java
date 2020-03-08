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
		new RottenFoodTask(plugin);
		new RottenFoodInfluence(plugin);
		if(plugin.getConfig().getBoolean("food.rotten.enable"))
			new Hunger(plugin);
	}
}
