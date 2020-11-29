package ute.api;

import ute.cap.HudProvider;
import ute.world.WorldProvider;
import org.bukkit.World;

public class WorldApi {
    public static String getSeasonColor(World world) {
        return HudProvider.yaml.getString("seasonColor." + getSeason(world).name(), "");
    }

    public static String getName(WorldProvider.Season season) {
        return season.getName();
    }

    public static WorldProvider.Season getSeason(World world) {
        final WorldProvider.IWorld status = WorldProvider.worldStates.get(world.getName());
        if (status != null)
            return status.season;
        return WorldProvider.Season.NULL;
    }

    public static int getDay(World world) {
        final WorldProvider.IWorld status = WorldProvider.worldStates.get(world.getName());
        if (status != null)
            return status.day;
        return -1;
    }
}
