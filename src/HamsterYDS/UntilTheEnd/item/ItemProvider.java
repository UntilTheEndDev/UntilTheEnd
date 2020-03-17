package HamsterYDS.UntilTheEnd.item;

import java.io.File;
import java.util.HashMap;
import java.util.List;
a
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class ItemProvider {
    public static HashMap<EntityType, HashMap<String,Double>> drops = new HashMap<EntityType, HashMap<String,Double>>();
    public static YamlConfiguration yaml;

    public static void loadDrops() {
        File file = new File(ItemManager.plugin.getDataFolder(), "drops.yml");
        if(!file.exists())
        	ItemManager.plugin.saveResource("drops.yml", true);
        yaml = YamlConfiguration.loadConfiguration(file);

        for (String path : yaml.getKeys(false)) {
            List<String> items=yaml.getStringList(path+".items");
            List<Double> percents=yaml.getDoubleList(path+".percents");
            HashMap<String,Double> drop=new HashMap<String,Double>();
            for(int index=0;index<items.size();index++) {
            	drop.put(items.get(index),percents.get(index));
            }
            try {
            	drops.put(EntityType.valueOf(path),drop);
            }
            catch(Exception exception) {
            	System.out.println("drops.yml下实体"+path+"读取错误，请检查！");
            }
        }
    }
}
