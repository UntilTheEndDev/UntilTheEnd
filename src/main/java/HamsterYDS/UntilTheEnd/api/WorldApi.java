package HamsterYDS.UntilTheEnd.api;

import HamsterYDS.UntilTheEnd.cap.HudProvider;
import HamsterYDS.UntilTheEnd.world.WorldProvider;
import HamsterYDS.UntilTheEnd.world.WorldProvider.Season;
import org.bukkit.World;

public class WorldApi {
    public static String getSeasonColor(World world) {
        return HudProvider.yaml.getString("seasonColor." + getSeason(world).name(), "");
    }

    public static String getName(Season season) {
        return season.getName();
    }

    public static Season getSeason(World world) {
        final WorldProvider.IWorld status = WorldProvider.worldStates.get(world.getName());
        if (status != null)
            return status.season;
        return Season.NULL;
    }

    public static int getDay(World world) {
        final WorldProvider.IWorld status = WorldProvider.worldStates.get(world.getName());
        if (status != null)
            return status.day;
        return -1;
    }
}
