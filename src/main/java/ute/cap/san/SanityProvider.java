package ute.cap.san;

import java.util.HashMap;

import ute.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

public class SanityProvider {
    public static HashMap<EntityType, Integer> creatureAura = new HashMap<EntityType, Integer>();

    public static void loadAura() {
        for (String path : Sanity.yaml.getKeys(true)) {
            if (path.equalsIgnoreCase("creatureAura")) continue;
            if (path.startsWith("creatureAura")) {
                int san = Sanity.yaml.getInt(path);
                path = path.replace("creatureAura.", "");
                EntityType type = EntityType.valueOf(path);
                Bukkit.getConsoleSender().sendMessage(UTEi18n.parse("cap.san.provider.aura", path, String.valueOf(san)));
                creatureAura.put(type, san);
            }
        }
    }
}
