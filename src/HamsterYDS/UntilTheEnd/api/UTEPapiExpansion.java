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
        } else if (identifier.equals("san")) {
            return String.valueOf(MathHelper.p2(PlayerManager.check(player, CheckType.SANITY)));
        } else if (identifier.equals("tem")) {
            return String.valueOf(MathHelper.p2(PlayerManager.check(player, CheckType.TEMPERATURE)));
        } else if (identifier.equals("hum")) {
            return String.valueOf(MathHelper.p2(PlayerManager.check(player, CheckType.HUMIDITY)));
        } else if (identifier.equals("tir")) {
            return String.valueOf(MathHelper.p2(PlayerManager.check(player, CheckType.TIREDNESS)));
        } else if (identifier.equals("season")) {
            return WorldApi.getName(WorldApi.getSeason(player.getWorld()));
        } else if (identifier.equals("role")) {
            return PlayerApi.getRole(player).name;
        } else if (identifier.equals("sanmax")) {
        	return String.valueOf(PlayerManager.check(player, CheckType.SANMAX));
        } else if (identifier.equals("healthmax")) {
        	return String.valueOf(PlayerManager.check(player, CheckType.HEALTHMAX));
        } else if (identifier.equals("level")) {
        	return String.valueOf(PlayerManager.check(player,CheckType.LEVEL));
        } else if (identifier.equals("damagelevel")) {
        	return String.valueOf(PlayerManager.check(player,CheckType.DAMAGELEVEL));
        } else if (identifier.equals("day")) {
            return String.valueOf(WorldApi.getDay(player.getWorld()));
        } else if (identifier.equals("sanitycolor")) {
            return String.valueOf(PlayerApi.getSanityColor(player));
        } else if (identifier.equals("temperaturecolor")) {
            return String.valueOf(PlayerApi.getTemperatureColor(player));
        } else if (identifier.equals("humiditycolor")) {
            return String.valueOf(PlayerApi.getHumidityColor(player));
        } else if (identifier.equals("tirednesscolor")) {
            return String.valueOf(PlayerApi.getTirednessColor(player));
        } else if (identifier.equals("seasoncolor")) {
            return String.valueOf(WorldApi.getSeasonColor(player.getWorld()));
        } else if (identifier.equals("sanitytend")) {
            return String.valueOf(PlayerApi.getChangingTend(player, "san"));
        } else if (identifier.equals("temperaturetend")) {
            return String.valueOf(PlayerApi.getChangingTend(player, "tem"));
        } else if (identifier.equals("humiditytend")) {
            return String.valueOf(PlayerApi.getChangingTend(player, "hum"));
        } else if (identifier.equals("tirednesstend")) {
            return String.valueOf(PlayerApi.getChangingTend(player, "tir"));
        } else if (identifier.equals("sanitybar")) {
            return PlayerApi.getSanityBar(player);
        } else if (identifier.equals("humiditybar")) {
            return PlayerApi.getHumidityBar(player);
        } else if (identifier.equals("temperaturebar")) {
            return PlayerApi.getTemperatureBar(player);
        } else if (identifier.equals("tirednessbar")) {
            return PlayerApi.getTirednessBar(player);
        } else {
        	return identifier;
        }
    }
}
