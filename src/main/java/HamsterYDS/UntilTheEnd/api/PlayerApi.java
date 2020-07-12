package HamsterYDS.UntilTheEnd.api;

import HamsterYDS.UntilTheEnd.cap.HudProvider;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;
import HamsterYDS.UntilTheEnd.player.role.Roles;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public class PlayerApi {
    private static final BiFunction<Player, String, String> placeholderAPI_fun;

    static {
        BiFunction<Player, String, String> placeholderAPI_ = (p, v) -> v;
        try {
            placeholderAPI_ = PlaceholderAPI::setPlaceholders;
        } catch (Throwable ignored) {
        }
        placeholderAPI_fun = placeholderAPI_;
    }

    public static String getPAPI(Player player, String line) {
        return placeholderAPI_fun.apply(player, line);
    }

    public static String getChangingTend(Player player, String type) {
        if (type.equalsIgnoreCase("san")) return HudProvider.sanity.get(player.getUniqueId());
        if (type.equalsIgnoreCase("tem")) return HudProvider.temperature.get(player.getUniqueId());
        if (type.equalsIgnoreCase("hum")) return HudProvider.humidity.get(player.getUniqueId());
        if (type.equalsIgnoreCase("tir")) return HudProvider.tiredness.get(player.getUniqueId());
        return "";
    }

    public static String getSanityColor(Player player) {
        double san = getValue(player, "san");
        if (san >= 120) return HudProvider.yaml.getString("sanityColor.120");
        if (san >= 90) return HudProvider.yaml.getString("sanityColor.90");
        if (san >= 60) return HudProvider.yaml.getString("sanityColor.60");
        if (san >= 30) return HudProvider.yaml.getString("sanityColor.30");
        if (san >= 0) return HudProvider.yaml.getString("sanityColor.0");
        return "";
    }

    public static String getHumidityColor(Player player) {
        double hum = getValue(player, "hum");
        if (hum <= 5) return HudProvider.yaml.getString("humidityColor.5");
        if (hum <= 15) return HudProvider.yaml.getString("humidityColor.15");
        if (hum <= 25) return HudProvider.yaml.getString("humidityColor.25");
        return "";
    }

    public static String getTemperatureColor(Player player) {
        double tem = getValue(player, "tem");
        if (tem <= 15) return HudProvider.yaml.getString("temperatureColor.15");
        if (tem <= 50) return HudProvider.yaml.getString("temperatureColor.50");
        if (tem <= 75) return HudProvider.yaml.getString("temperatureColor.75");
        return "";
    }

    public static String getTirednessColor(Player player) {
        double tir = getValue(player, "tir");
        if (tir <= 25) return HudProvider.yaml.getString("tirednessColor.25");
        if (tir <= 50) return HudProvider.yaml.getString("tirednessColor.50");
        if (tir <= 75) return HudProvider.yaml.getString("tirednessColor.75");
        if (tir <= 100) return HudProvider.yaml.getString("tirednessColor.100");
        return "";
    }

    public static double getValue(Player player, String type) {
        return PlayerManager.check(player, type);
    }

    public static void setValue(Player player, String type, int value) {
        PlayerManager.change(player, type, value);
    }

    public static Roles getRole(Player player) {
        return PlayerManager.checkRole(player);
    }

    public static void changeRole(Player player, Roles role) {
        PlayerManager.changeRole(player, role);
    }

    public static String getSanityBar(Player player) {
        double san = PlayerManager.check(player, CheckType.SANITY);
        double sanMax = PlayerManager.check(player, CheckType.SANMAX);
        return getMessageBar(player, san, sanMax, HudProvider.yaml.getString("messageBar.sancolor"));
    }

    public static String getHumidityBar(Player player) {
        double hum = PlayerManager.check(player, CheckType.HUMIDITY);
        double humMax = 100;
        return getMessageBar(player, hum, humMax, HudProvider.yaml.getString("messageBar.humcolor"));
    }

    public static String getTemperatureBar(Player player) {
        double tem = PlayerManager.check(player, CheckType.TEMPERATURE) + 5;
        double temMax = 80;
        return getMessageBar(player, tem, temMax, HudProvider.yaml.getString("messageBar.temcolor"));
    }

    public static String getTirednessBar(Player player) {
        double tir = PlayerManager.check(player, CheckType.TIREDNESS);
        double tirMax = 100;
        return getMessageBar(player, tir, tirMax, HudProvider.yaml.getString("messageBar.tircolor"));
    }

    public static String getMessageBar(Player player, double value, double maxValue, String color1) {
        String bar = "";
        String newBar = color1;
        int length = HudProvider.yaml.getInt("messageBar.length");
        String color2 = HudProvider.yaml.getString("messageBar.nocolor");
        String unit = HudProvider.yaml.getString("messageBar.unit");
        for (int i = 0; i < length; i++) bar += unit;
        boolean flag = true;
        if (value == 0) newBar += color2;
        for (int i = 0; i < bar.length(); i++) {
            double percent = (double) i / (double) bar.length();
            newBar += bar.charAt(i);
            if (percent >= value / maxValue && flag) {
                newBar += color2;
                flag = false;
            }
        }
        newBar += "§r";
        return newBar;
    }
}
