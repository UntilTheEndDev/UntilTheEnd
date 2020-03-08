package HamsterYDS.UntilTheEnd.item;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class ItemProvider {
    public static HashMap<EntityType, String> drops = new HashMap<EntityType, String>();
    public static HashMap<EntityType, Double> percents = new HashMap<EntityType, Double>();

    public static ItemStack getItem(String itemName) {
        return ItemManager.namesAndItems.get(ItemManager.idsAndNames.get(itemName));
    }

    public static void loadDrops() {
        File file = new File(ItemManager.plugin.getDataFolder(), "drops.yml");
        ItemManager.plugin.saveResource("drops.yml", true);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for (String path : yaml.getKeys(false)) {
            EntityType type = EntityType.valueOf(path);
            String itemName = yaml.getString(path + ".dropitem");
            double percent = yaml.getDouble(path + ".percent");
            drops.put(type, itemName);
            percents.put(type, percent);
        }
    }
}
