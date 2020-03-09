package HamsterYDS.UntilTheEnd.cap.hum;

import java.util.HashMap;

import HamsterYDS.UntilTheEnd.internal.UTEi18n;
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
                System.out.println(UTEi18n.parse("cap.hum.provider.rule", String.valueOf(to), String.valueOf(from)));
                moistness.put(sf, tm);
                driness.put(tm, sf);
            }
        }
    }
}
