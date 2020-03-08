package HamsterYDS.UntilTheEnd.cap.hum;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class HumidityProvider {
    public static HashMap<Material, Material> moistness = new HashMap<>();
    public static HashMap<Material, Material> driness = new HashMap<>();

    public static void loadConfig() {
        final ConfigurationSection section = Humidity.yaml.getConfigurationSection("wetBlocks");
        if (section != null) {
            for (String from : section.getKeys(false)) {
                String to = section.getString(from);
                Material sf = Material.valueOf(from);
                Material tm = Material.valueOf(to);
                System.out.println("检测到带有潮湿方块变化" + to + "->" + from);
                moistness.put(sf, tm);
                driness.put(tm, sf);
            }
        }
    }
}
