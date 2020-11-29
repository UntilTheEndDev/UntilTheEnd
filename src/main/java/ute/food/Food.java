package ute.food;

import ute.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Food {

    public Food(UntilTheEnd plugin) {
        if (plugin.getConfig().getBoolean("food.rotten.invenable"))
            new RottenFoodTask(plugin);
        if (plugin.getConfig().getBoolean("food.rotten.guienable")) {
            RottenFoodEvents events = new RottenFoodEvents();
            plugin.getServer().getPluginManager().registerEvents(events, plugin);
            plugin.getServer().getScheduler().runTaskTimer(plugin, events, 0,
                    plugin.getConfig().getLong("food.rotten.guispeed"));
        }
        new RottenFoodInfluence(plugin);
        if (plugin.getConfig().getBoolean("food.hunger.enable"))
            new Hunger(plugin);
    }
}
