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
		if(plugin.getConfig().getBoolean("food.rotten.enable")) {
			new RottenFood1(plugin);
			new RottenFood2(plugin);
		}
		if(plugin.getConfig().getBoolean("food.hunger.enable"))
			new Hunger(plugin);
	}
}
