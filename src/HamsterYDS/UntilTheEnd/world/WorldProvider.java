package HamsterYDS.UntilTheEnd.world;

import java.io.IOException;
import java.util.HashMap;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

public class WorldProvider {
    public static HashMap<String, IWorld> worldStates = new HashMap<String, IWorld>();

    public static void loadWorlds() {
        for (org.bukkit.World world : Config.enableWorlds) {
            String worldName = world.getName();
            if (!World.yaml.getKeys(false).contains(worldName)) {
                worldStates.put(worldName, new IWorld(Season.AUTUMN, 1, Season.AUTUMN.newLoop()));
                World.yaml.set(worldName + ".season", Season.AUTUMN.name);
                World.yaml.set(worldName + ".day", 1);
                try {
                    World.yaml.save(World.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Season season = Season.getSeason(World.yaml.getString(worldName + ".season"));
                int day = World.yaml.getInt(worldName + ".day");
                int loop = World.yaml.getInt(worldName + ".loop", season.newLoop());
                worldStates.put(worldName, new IWorld(season, day, loop));
            }
        }
    }

    public static void saveWorlds() {
        for (org.bukkit.World world : Config.enableWorlds) {
            IWorld state = worldStates.get(world.getName());
            World.yaml.set(world.getName() + ".season", state.season.name);
            World.yaml.set(world.getName() + ".day", state.day);
            World.yaml.set(world.getName() + ".loop", state.loop);
            try {
                World.yaml.save(World.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public enum Season {
        SPRING("春天", 1),
        SUMMER("夏天", 2),
        AUTUMN("秋天", 3),
        WINTER("冬天", 0),
        NULL("未启用", 4);
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
            switch (name) {
                case "春天":
                    return SPRING;
                case "夏天":
                    return SUMMER;
                case "秋天":
                    return AUTUMN;
                case "冬天":
                    return WINTER;
                default:
                    return AUTUMN;
            }
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
