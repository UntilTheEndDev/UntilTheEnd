package ute.world;

import ute.Config;
import ute.Logging;
import ute.UntilTheEnd;
import ute.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class WorldProvider {
    public static HashMap<String, IWorld> worldStates = new HashMap<>();

    private static boolean initialize(org.bukkit.World world) {
        final ConfigurationSection section = World.yaml.getConfigurationSection(world.getName());
        if (section != null) {
            Season season = Season.getSeason(section.getString("season"));
            int day = section.getInt("day");
            int loop = section.getInt("loop", season.newLoop());
            worldStates.put(world.getName(), new IWorld(season, day, loop));
            return false;
        }
        int loop = Season.AUTUMN.newLoop();
        worldStates.put(world.getName(), new IWorld(Season.AUTUMN, 1, loop));
        final ConfigurationSection store = World.yaml.createSection(world.getName());
        store.set("season", Season.AUTUMN.name());
        store.set("day", 1);
        store.set("loop", loop);
        return true;
    }

    public static void loadWorlds() {
        boolean needUpdate = false;
        for (org.bukkit.World world : Config.enableWorlds) {
            String worldName = world.getName();
            needUpdate |= initialize(world);
            if (!World.yaml.getKeys(false).contains(worldName)) {
                worldStates.put(worldName, new IWorld(Season.AUTUMN, 1, Season.AUTUMN.newLoop()));
                World.yaml.set(worldName + ".season", Season.AUTUMN.name);
                World.yaml.set(worldName + ".day", 1);
            }
        }
        if (needUpdate) storeData();
    }

    private static void storeData() {
        try {
            World.yaml.save(World.file);
        } catch (IOException e) {
            Logging.getLogger().log(Level.SEVERE, "Failed to update world data", e);
        }
    }

    public static void registerListener() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler()
            public void on(WorldLoadEvent event) {
                if (initialize(event.getWorld()))
                    storeData();
            }
        }, UntilTheEnd.getInstance());
    }

    public static void saveWorlds() {
        for (org.bukkit.World world : Config.enableWorlds) {
            IWorld state = worldStates.get(world.getName());
            if (state == null) { // Hum????????
                continue;
            }
            World.yaml.set(world.getName() + ".season", state.season.name());
            World.yaml.set(world.getName() + ".day", state.day);
            World.yaml.set(world.getName() + ".loop", state.loop);
        }
        storeData();
    }

    public enum Season {
        SPRING(UTEi18n.cache("season.spring"), 1),
        SUMMER(UTEi18n.cache("season.summer"), 2),
        AUTUMN(UTEi18n.cache("season.autumn"), 3),
        WINTER(UTEi18n.cache("season.winter"), 0),
        NULL(UTEi18n.cache("season.disable"), 4);
        String name;
        int keepTime;
        int next;

        public Season next() {
            return CACHED[next];
        }

        Season(String name, int next) {
            this.name = name;
            this.next = next;
        }

        static Season getSeason(String name) {
            return found(name);
        }

        private void load() {
            keepTime = UntilTheEnd.getInstance().getConfig().getInt("world.season." + name().toLowerCase(), 20);
        }

        private static final Season[] CACHED = values();

        static {
            reload();
        }

        public static void reload() {
            for (Season s : CACHED) s.load();
        }

        public static Season found(String n) {
            for (Season s : CACHED) {
                if (s.name().equalsIgnoreCase(n) || s.name.equalsIgnoreCase(n)) return s;
            }
            return null;
        }

        public int newLoop() {
            return Math.max(1, keepTime + (int) ((Math.random() - 0.5) * 10));
        }

        public String getName() {
            return name;
        }
    }

    public static class IWorld {
        public Season season;
        public int day;
        public int loop;

        public IWorld(Season season, int day, int loop) {
            this.season = season;
            this.day = day;
            this.loop = loop;
        }
    }
}
