package HamsterYDS.UntilTheEnd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class Config {
    public static UntilTheEnd plugin = UntilTheEnd.getInstance();
    public static YamlConfiguration yaml = null;
    public static List<World> enableWorlds = new ArrayList<>();
    private static Set<String> disables = new HashSet<>();

    public static void registerWorld(World world) {
        final String wn = world.getName();
        if (disables.stream().noneMatch(name -> name.equalsIgnoreCase(wn))) {
            enableWorlds.add(world);
        }
    }

    public Config(UntilTheEnd plugin) {
        disables.addAll(plugin.getConfig().getStringList("disableWorlds"));
        Bukkit.getWorlds().forEach(Config::registerWorld);
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            void onWorldLoad(WorldLoadEvent event) {
                registerWorld(event.getWorld());
            }

			@EventHandler
            void onWorldUnload(WorldUnloadEvent event) {
                enableWorlds.remove(event.getWorld());
            }
        }, plugin);
        File file = new File(plugin.getDataFolder(), plugin.getConfig().getString("language"));
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public static String getLang(String path) {
        return (yaml.getString("prefix") + yaml.getString(path, path));
    }

    public static YamlConfiguration autoUpdateConfigs(String name) {
        File file = new File(plugin.getDataFolder(), name);
        final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        plugin.saveResource(name, true);
        YamlConfiguration newYaml = YamlConfiguration.loadConfiguration(file);
        for (String path : yaml.getKeys(true)) {
            Object obj = yaml.get(path);
            newYaml.set(path, obj);
        }
        try {
            newYaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return YamlConfiguration.loadConfiguration(file);
    }
}
