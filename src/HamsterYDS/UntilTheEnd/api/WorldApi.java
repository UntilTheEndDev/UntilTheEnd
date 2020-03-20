package HamsterYDS.UntilTheEnd.api;

import org.bukkit.World;

import HamsterYDS.UntilTheEnd.cap.HudProvider;
import HamsterYDS.UntilTheEnd.world.WorldProvider;
import HamsterYDS.UntilTheEnd.world.WorldProvider.Season;

public class WorldApi {
    public static String getSeasonColor(World world) {
        return HudProvider.yaml.getString("seasonColor." + getSeason(world).name(), "");
    }

    public static String getName(Season season) {
        return season.getName();
    }

    public static Season getSeason(World world) {
        if (WorldProvider.worldStates.containsKey(world.getName()))
            return WorldProvider.worldStates.get(world.getName()).season;
        return Season.NULL;
    }

    public static int getDay(World world) {
        if (WorldProvider.worldStates.containsKey(world.getName()))
            return WorldProvider.worldStates.get(world.getName()).day;
        return -1;
    }
}
