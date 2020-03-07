package HamsterYDS.UntilTheEnd.world;

import java.io.IOException;
import java.util.HashMap;

import HamsterYDS.UntilTheEnd.Config;

public class WorldProvider {
    public static HashMap<String, IWorld> worldStates = new HashMap<String, IWorld>();

    public static void loadWorlds() {
        for (org.bukkit.World world : Config.enableWorlds) {
            String worldName = world.getName();
            if (!World.yaml.getKeys(false).contains(worldName)) {
                worldStates.put(worldName, new IWorld(Season.AUTUMN, 1));
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
                worldStates.put(worldName, new IWorld(season, day));
            }
        }
    }

    public static void saveWorlds() {
        for (org.bukkit.World world : Config.enableWorlds) {
            IWorld state = worldStates.get(world.getName());
            World.yaml.set(world.getName() + ".season", state.season.name);
            World.yaml.set(world.getName() + ".day", state.day);
            try {
                World.yaml.save(World.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public enum Season {
        SPRING("春天"), SUMMER("夏天"), AUTUMN("秋天"), WINTER("冬天"), NULL("未启用");
        String name;

        Season(String name) {
            this.name = name;
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

        public static Season found(String n) {
            for (Season s : values()) {
                if (s.name().equalsIgnoreCase(n) || s.name.equalsIgnoreCase(n)) return s;
            }
            return null;
        }
    }

    public static class IWorld {
        public Season season;
        public int day;

        public IWorld(Season season, int day) {
            this.season = season;
            this.day = day;
        }
    }
}
