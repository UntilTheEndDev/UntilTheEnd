package ute.api;

import ute.internal.MathHelper;
import ute.player.PlayerManager;
import ute.player.PlayerManager.CheckType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Create at 2020/3/7 23:04
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.papi
 */
public class UTEPapiExpansion extends PlaceholderExpansion {

    public @NotNull String getAuthor() {
        return "[南外丶仓鼠,瑞瑞瑞瑞阿,Karlatemp]";
    }

    public @NotNull String getIdentifier() {
        return "ute";
    }

    public @NotNull String getVersion() {
        return "5.6";
    }

    @Override
    public String onRequest(OfflinePlayer off_player, @NotNull String identifier) {
        if (off_player == null) return null;
        Player player = off_player.getPlayer();
        if (player == null) {
            return "";
        }
        switch (identifier) {
            case "san":
                return String.valueOf(MathHelper.p2(PlayerApi.SanityOperations.getSanity(player)));
            case "tem":
                return String.valueOf(MathHelper.p2(PlayerApi.TemperatureOperations.getTemperature(player)));
            case "hum":
                return String.valueOf(MathHelper.p2(PlayerApi.HumidityOperations.getHumidity(player)));
            case "tir":
                return String.valueOf(MathHelper.p2(PlayerApi.TirednessOperations.getTiredness(player)));
            case "role":
                return PlayerApi.RoleOperations.getRole(player).name;
            case "sanmax":
                return String.valueOf(PlayerApi.SanityOperations.getMaxSanity(player));
                //TODO
            case "healthmax":
                return String.valueOf(PlayerManager.check(player, CheckType.HEALTHMAX));
            case "level":
                return String.valueOf(PlayerManager.check(player, CheckType.LEVEL));
            case "damagelevel":
                return String.valueOf(PlayerManager.check(player, CheckType.DAMAGELEVEL));
                //TODO
            case "sanitycolor":
                return String.valueOf(PlayerApi.SanityOperations.getSanityColor(player));
            case "temperaturecolor":
                return String.valueOf(PlayerApi.TemperatureOperations.getTemperatureColor(player));
            case "humiditycolor":
                return String.valueOf(PlayerApi.HumidityOperations.getHumidityColor(player));
            case "tirednesscolor":
                return String.valueOf(PlayerApi.TirednessOperations.getTirednessColor(player));
            case "sanitytend":
                return String.valueOf(PlayerApi.SanityOperations.getChangingTend(player, "san"));
            case "temperaturetend":
                return String.valueOf(PlayerApi.TemperatureOperations.getChangingTend(player, "tem"));
            case "humiditytend":
                return String.valueOf(PlayerApi.HumidityOperations.getChangingTend(player, "hum"));
            case "tirednesstend":
                return String.valueOf(PlayerApi.TirednessOperations.getChangingTend(player, "tir"));
            case "sanitybar":
                return PlayerApi.SanityOperations.getSanityBar(player);
            case "humiditybar":
                return PlayerApi.HumidityOperations.getHumidityBar(player);
            case "temperaturebar":
                return PlayerApi.TemperatureOperations.getTemperatureBar(player);
            case "tirednessbar":
                return PlayerApi.TirednessOperations.getTirednessBar(player);

            case "season":
                return WorldApi.getName(WorldApi.getSeason(player.getWorld()));
            case "day":
                return String.valueOf(WorldApi.getDay(player.getWorld()));
            case "seasoncolor":
                return String.valueOf(WorldApi.getSeasonColor(player.getWorld()));
            default:
                return identifier;
        }
    }
}
