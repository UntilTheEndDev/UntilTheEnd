package ute.item;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import ute.Logging;
import ute.internal.UTEi18n;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class ItemProvider { 
    public static HashMap<EntityType, HashMap<String, Double>> entityDrops = new HashMap<EntityType, HashMap<String, Double>>();
    public static HashMap<Material, HashMap<String, Double>> blockDrops = new HashMap<Material, HashMap<String, Double>>();
    public static YamlConfiguration yaml;

    public static void loadDrops() {
        File file = new File(ItemManager.plugin.getDataFolder(), "drops.yml");
        if (!file.exists())
            ItemManager.plugin.saveResource("drops.yml", true);
        yaml = YamlConfiguration.loadConfiguration(file);

        for (String path : yaml.getKeys(false)) {
            List<String> items = yaml.getStringList(path + ".dropitems");
            List<Double> percents = yaml.getDoubleList(path + ".percents");
            HashMap<String, Double> drop = new HashMap<String, Double>();
            for (int index = 0; index < items.size(); index++) {
                drop.put(items.get(index), percents.get(index));
                //方块也输出“生物”  TODO
                Logging.getLogger().log(Level.INFO,
                        UTEi18n.parse("item.provider.drops.rule", path, String.valueOf(items.get(index)), String.valueOf(percents.get(index)))
                );
            }
            try {
                entityDrops.put(EntityType.valueOf(path), drop);
            } catch (Exception exception) {
            	 try {
                     blockDrops.put(Material.valueOf(path), drop);
                 } catch (Exception exception2) {
                     Logging.getLogger().log(Level.SEVERE, "Failed to load drops [" + path + "] from drops.yml", exception);
                 }
            }
        }
    }
}
