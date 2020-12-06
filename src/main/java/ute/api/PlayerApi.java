package ute.api;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ute.cap.HudProvider;
import ute.api.event.cap.HumidityChangeEvent;
import ute.api.event.cap.SanityChangeEvent;
import ute.api.event.cap.TemperatureChangeEvent;
import ute.api.event.cap.TirednessChangeEvent;
import ute.player.PlayerManager;
import ute.player.PlayerManager.CheckType;
import ute.player.role.Roles;

import java.util.function.BiFunction;

public class PlayerApi {
    public static class PapiOperations{
        private static final BiFunction<OfflinePlayer, String, String> placeholderAPI_fun;

        static {
            BiFunction<OfflinePlayer, String, String> placeholderAPI_ = (p, v) -> v;
            try {
                placeholderAPI_ = PlaceholderAPI::setPlaceholders;
            } catch (Throwable ignored) {
            }
            placeholderAPI_fun = placeholderAPI_;
        }

        public static String getPAPI(Player player, String line) {
            return placeholderAPI_fun.apply(player, line);
        }
    }

    public static class RoleOperations{
        public static Roles getRole(Player player) {
            return PlayerManager.checkRole(player);
        }

        public static void changeRole(Player player, Roles role) {
            PlayerManager.changeRole(player, role);
        }
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

    public static class TirednessOperations{
        public static boolean changeTiredness(Player player, TirednessChangeEvent.ChangeCause cause, double change){
            TirednessChangeEvent event=new TirednessChangeEvent(player,cause,change);
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()){
                PlayerManager.change(player,CheckType.TIREDNESS,change);
            }
            return event.isCancelled();
        }
        public static double getTiredness(Player player){
            return PlayerManager.check(player,CheckType.TIREDNESS);
        }
        public static String getTirednessBar(Player player) {
            double tir = getTiredness(player);
            double tirMax = 100;
            return getMessageBar(player, tir, tirMax, HudProvider.yaml.getString("messageBar.tircolor"));
        }
        public static String getTirednessColor(Player player) {
            double tir = getTiredness(player);
            if (tir <= 25) return HudProvider.yaml.getString("tirednessColor.25");
            if (tir <= 50) return HudProvider.yaml.getString("tirednessColor.50");
            if (tir <= 75) return HudProvider.yaml.getString("tirednessColor.75");
            if (tir <= 100) return HudProvider.yaml.getString("tirednessColor.100");
            return "";
        }
        public static String getChangingTend(Player player, String type) {
            return HudProvider.tiredness.get(player.getUniqueId());
        }
    }

    public static class TemperatureOperations{
        public static boolean changeTemperature(Player player, TemperatureChangeEvent.ChangeCause cause, double change){
            TemperatureChangeEvent event=new TemperatureChangeEvent(player,cause,change);
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()){
                PlayerManager.change(player,CheckType.TEMPERATURE,change);
            }
            return event.isCancelled();
        }
        public static double getTemperature(Player player){
            return PlayerManager.check(player,CheckType.TEMPERATURE);
        }
        public static String getTemperatureBar(Player player) {
            double tem = getTemperature(player) + 5;
            double temMax = 80;
            return getMessageBar(player, tem, temMax, HudProvider.yaml.getString("messageBar.temcolor"));
        }
        public static String getTemperatureColor(Player player) {
            double tem = getTemperature(player);
            if (tem <= 15) return HudProvider.yaml.getString("temperatureColor.15");
            if (tem <= 50) return HudProvider.yaml.getString("temperatureColor.50");
            if (tem <= 75) return HudProvider.yaml.getString("temperatureColor.75");
            return "";
        }
        public static String getChangingTend(Player player, String type) {
            return HudProvider.temperature.get(player.getUniqueId());
        }
    }

    public static class HumidityOperations{
        public static boolean changeHumidity(Player player, HumidityChangeEvent.ChangeCause cause, double change){
            HumidityChangeEvent event=new HumidityChangeEvent(player,cause,change);
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()){
                PlayerManager.change(player,CheckType.HUMIDITY,change);
            }
            return event.isCancelled();
        }
        public static double getHumidity(Player player){
            return PlayerManager.check(player,CheckType.HUMIDITY);
        }
        public static String getHumidityBar(Player player) {
            double hum = getHumidity(player);
            double humMax = 100;
            return getMessageBar(player, hum, humMax, HudProvider.yaml.getString("messageBar.humcolor"));
        }
        public static String getHumidityColor(Player player) {
            double hum =getHumidity(player);
            if (hum <= 5) return HudProvider.yaml.getString("humidityColor.5");
            if (hum <= 15) return HudProvider.yaml.getString("humidityColor.15");
            if (hum <= 25) return HudProvider.yaml.getString("humidityColor.25");
            return "";
        }
        public static String getChangingTend(Player player, String type) {
            return HudProvider.humidity.get(player.getUniqueId());
        }
    }


    public static class SanityOperations{
        public static boolean changeSanity(Player player, SanityChangeEvent.ChangeCause cause, double change){
            SanityChangeEvent event=new SanityChangeEvent(player,cause,change);
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()){
                PlayerManager.change(player,CheckType.SANITY,change);
            }
            return event.isCancelled();
        }
        public static double getSanity(Player player){
            return PlayerManager.check(player,CheckType.SANITY);
        }
        public static double getMaxSanity(Player player){
            return PlayerManager.check(player,CheckType.SANMAX);
        }
        public static String getSanityBar(Player player) {
            double san = getSanity(player);
            double sanMax = getMaxSanity(player);
            return getMessageBar(player, san, sanMax, HudProvider.yaml.getString("messageBar.sancolor"));
        }
        public static String getSanityColor(Player player) {
            double san = getSanity(player);
            if (san >= 120) return HudProvider.yaml.getString("sanityColor.120");
            if (san >= 90) return HudProvider.yaml.getString("sanityColor.90");
            if (san >= 60) return HudProvider.yaml.getString("sanityColor.60");
            if (san >= 30) return HudProvider.yaml.getString("sanityColor.30");
            if (san >= 0) return HudProvider.yaml.getString("sanityColor.0");
            return "";
        }
        public static String getChangingTend(Player player, String type) {
            return HudProvider.sanity.get(player.getUniqueId());
        }
    }
}
