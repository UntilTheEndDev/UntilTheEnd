package ute.food;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import ute.Config;
import ute.UntilTheEnd;
import ute.crops.CropProvider;
import ute.internal.UTEi18n;

public class Food {
    static YamlConfiguration yaml;

    public Food(UntilTheEnd plugin) {
        yaml = Config.autoUpdateConfigs("food.yml");
        Bukkit.getServer().getConsoleSender().sendMessage(UTEi18n.cacheWithPrefix("cap.crops.provider.loading"));
        CropProvider.loadConfig();
        if (yaml.getBoolean("food.rotten.invenable"))
            new RottenFoodTask(plugin);
        if (yaml.getBoolean("food.rotten.guienable")) {
            RottenFoodEvents events = new RottenFoodEvents();
            plugin.getServer().getPluginManager().registerEvents(events, plugin);
            plugin.getServer().getScheduler().runTaskTimer(plugin, events, 0,
                    yaml.getLong("food.rotten.guispeed"));
        }
        new RottenFoodInfluence(plugin);
        if (yaml.getBoolean("food.hunger.enable"))
            new Hunger(plugin);
    }
}
