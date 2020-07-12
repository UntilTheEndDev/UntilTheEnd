package HamsterYDS.UntilTheEnd.api;

import HamsterYDS.UntilTheEnd.internal.MathHelper;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * Create at 2020/3/7 23:04
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.papi
 */
public class UTEPapiExpansion extends PlaceholderExpansion {

    public String getAuthor() {
        return "[南外丶仓鼠,瑞瑞瑞瑞阿,Karlatemp]";
    }

    public String getIdentifier() {
        return "ute";
    }

    public String getVersion() {
        return "5.6";
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }
        switch (identifier) {
            case "san":
                return String.valueOf(MathHelper.p2(PlayerManager.check(player, CheckType.SANITY)));
            case "tem":
                return String.valueOf(MathHelper.p2(PlayerManager.check(player, CheckType.TEMPERATURE)));
            case "hum":
                return String.valueOf(MathHelper.p2(PlayerManager.check(player, CheckType.HUMIDITY)));
            case "tir":
                return String.valueOf(MathHelper.p2(PlayerManager.check(player, CheckType.TIREDNESS)));
            case "season":
                return WorldApi.getName(WorldApi.getSeason(player.getWorld()));
            case "role":
                return PlayerApi.getRole(player).name;
            case "sanmax":
                return String.valueOf(PlayerManager.check(player, CheckType.SANMAX));
            case "healthmax":
                return String.valueOf(PlayerManager.check(player, CheckType.HEALTHMAX));
            case "level":
                return String.valueOf(PlayerManager.check(player, CheckType.LEVEL));
            case "damagelevel":
                return String.valueOf(PlayerManager.check(player, CheckType.DAMAGELEVEL));
            case "day":
                return String.valueOf(WorldApi.getDay(player.getWorld()));
            case "sanitycolor":
                return String.valueOf(PlayerApi.getSanityColor(player));
            case "temperaturecolor":
                return String.valueOf(PlayerApi.getTemperatureColor(player));
            case "humiditycolor":
                return String.valueOf(PlayerApi.getHumidityColor(player));
            case "tirednesscolor":
                return String.valueOf(PlayerApi.getTirednessColor(player));
            case "seasoncolor":
                return String.valueOf(WorldApi.getSeasonColor(player.getWorld()));
            case "sanitytend":
                return String.valueOf(PlayerApi.getChangingTend(player, "san"));
            case "temperaturetend":
                return String.valueOf(PlayerApi.getChangingTend(player, "tem"));
            case "humiditytend":
                return String.valueOf(PlayerApi.getChangingTend(player, "hum"));
            case "tirednesstend":
                return String.valueOf(PlayerApi.getChangingTend(player, "tir"));
            case "sanitybar":
                return PlayerApi.getSanityBar(player);
            case "humiditybar":
                return PlayerApi.getHumidityBar(player);
            case "temperaturebar":
                return PlayerApi.getTemperatureBar(player);
            case "tirednessbar":
                return PlayerApi.getTirednessBar(player);
            default:
                return identifier;
        }
    }
}
